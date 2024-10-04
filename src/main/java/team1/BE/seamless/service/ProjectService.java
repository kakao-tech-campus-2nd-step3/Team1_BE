package team1.BE.seamless.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team1.BE.seamless.DTO.ProjectDTO;
import team1.BE.seamless.DTO.ProjectDTO.ProjectCreate;
import team1.BE.seamless.DTO.ProjectDTO.ProjectPeriod;
import team1.BE.seamless.DTO.ProjectDTO.ProjectUpdate;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.OptionEntity;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.ProjectOption;
import team1.BE.seamless.entity.UserEntity;
import team1.BE.seamless.mapper.ProjectMapper;
import team1.BE.seamless.repository.OptionRepository;
import team1.BE.seamless.repository.ProjectRepository;
import team1.BE.seamless.repository.UserRepository;
import team1.BE.seamless.util.errorException.BaseHandler;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;
    private final ProjectMapper projectMapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository,
        OptionRepository optionRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.optionRepository = optionRepository;
        this.projectMapper = projectMapper;
    }

    /*
    * @param param : 페이지네이션에 관한 parameter
    * @param email : 유저 토큰에서 추출한 email 정보
    * @return : 페이지네이션된 프로젝트들에 대한 정보
    * ProjectEntity에 매핑된 UserEntity의 email 정보가 일치하고 isDeleted가 false인 프로젝트를 페이지네이션 형식으로 반환
    * */
    public Page<ProjectEntity> getProjectList(ProjectDTO.getList param, String email) {
        return projectRepository.findAllByUserEntityEmailAndIsDeletedFalse(param.toPageable(),
            email);
    }

    /*
    * @param id : 프로젝트 Id
    * @return : 해당 Id의 프로젝트의 정보를 반환
    * repository 조회시 존재 하지 않을 경우 Throw Not Found
    * */
    public ProjectEntity getProject(long id) {
        return projectRepository.findById(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));
    }

    /*
    * @param id : 프로젝트 Id
    * @return : 해당 id를 가진 프로젝트에 참여한 팀원들의 목록
    * */
    public List<MemberEntity> getProjectMembers(long id) {
        ProjectEntity projectEntity = projectRepository.findById(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));
        return projectEntity.getMemberEntities();
    }

    /*
    * @param param : 페이지네이션에 관한 parameter
    * @param email : 유저 토큰에서 추출한 email 정보
    * @return : 프로젝트의 Id, name, startDate, endDate 정보를 페이지네이션*/
    public Page<ProjectPeriod> getProjectPeriod(ProjectDTO.getList param, String email) {
        return projectRepository.findByUserEntityEmailAndIsDeletedFalse(param.toPageable(), email);
    }

    /*
    * @param create : 프로젝트 생성 시 필요한 정보를 담은 DTO
    * @param email : 유저 토큰에서 추출한 email 정보
    * @return : 생성한 프로젝트의 정보
    * 플로우 :
    * email을 통해 유저가 존재하는 지 검증 ->
    * DTO에 담긴 optionEntity들의 id 정보들을 통해 OptionEntity조회 ->
    * OptionEntity을 ProjectOption으로 매핑 ->
    * 해당 정보를 가진 ProjectEntity를 생성 후 Repo에 save ->
    * 각 ProjectOption의 ProjectEntity field를 생성한 ProjectEntity로 설정
    * */
    @Transactional
    public ProjectEntity createProject(ProjectCreate create, String email) {
        UserEntity userEntity = userRepository.findByEmailAndIsDeleteFalse(email)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "사용자가 존재하지 않음"));

        List<OptionEntity> optionEntities = optionRepository.findByIdIn(create.getOptionIds());

        List<ProjectOption> projectOptions = optionEntities.stream()
            .map(optionEntity -> new ProjectOption(optionEntity.getName(), optionEntity))
            .toList();

        ProjectEntity projectEntity = projectRepository.save(
            projectMapper.toEntity(create, userEntity, projectOptions));
        projectOptions.forEach(
            option -> option.setProjectEntity(projectEntity)); //ProjectOption에 Project 매핑
        return projectEntity;
    }

    /*
    * @param id : 프로젝트 Id
    * @param update : 프로젝트 업데이트 시 필요한 정보를 담은 DTO
    * @return : 수정한 프로젝트의 정보
    * 플로우 :
    * 프로젝트가 존재하는지 검증 ->
    * 기존의 ProjectOption 리스트 초기화->
    * DTO에 담긴 Option id들을 통해 OptionEntity 조회 ->
    * OptionEntity를 ProjectOption으로 매핑 ->
    * ProjectOption 리스트에 추가 ->
    * 나머지 정보 업데이트 후 저장
    * */
    @Transactional
    public ProjectEntity updateProject(long id, ProjectUpdate update) {
        ProjectEntity projectEntity = projectRepository.findById(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));
        // 기존 옵션 목록
        List<ProjectOption> projectOptions = projectEntity.getProjectOptions();
        projectOptions.clear();

        // 새로운 옵션 추가
        List<OptionEntity> optionEntities = optionRepository.findByIdIn(update.getOptionIds());
        for (OptionEntity optionEntity : optionEntities) {
            ProjectOption projectOption = new ProjectOption(optionEntity.getName(), projectEntity,
                optionEntity);
            projectOptions.add(projectOption);
        }

        projectEntity.update(
            update.getName(),
            update.getStartDate(),
            update.getEndDate()
        );

        projectRepository.save(projectEntity);
        return projectEntity;
    }

    /*
    * @param id : 프로젝트 Id
    * @return : 삭제한 프로젝트의 Id
    * 프로젝트의 존재 검증 후 존재 시 삭제
    * */
    @Transactional
    public Long deleteProject(long id) {
        ProjectEntity projectEntity = projectRepository.findById(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));
        projectRepository.deleteById(projectEntity.getId());
        return projectEntity.getId();
    }

}