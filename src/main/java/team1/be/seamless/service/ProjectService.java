package team1.be.seamless.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team1.be.seamless.dto.OptionDTO.OptionDetail;
import team1.be.seamless.dto.ProjectDTO;
import team1.be.seamless.dto.ProjectDTO.ProjectCreate;
import team1.be.seamless.dto.ProjectDTO.ProjectDate;
import team1.be.seamless.dto.ProjectDTO.ProjectDetail;
import team1.be.seamless.dto.ProjectDTO.ProjectUpdate;
import team1.be.seamless.entity.OptionEntity;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.entity.ProjectOptionEntity;
import team1.be.seamless.entity.TaskEntity;
import team1.be.seamless.entity.UserEntity;
import team1.be.seamless.entity.enums.Role;
import team1.be.seamless.mapper.OptionMapper;
import team1.be.seamless.mapper.ProjectMapper;
import team1.be.seamless.repository.OptionRepository;
import team1.be.seamless.repository.ProjectOptionRepository;
import team1.be.seamless.repository.ProjectRepository;
import team1.be.seamless.repository.TaskRepository;
import team1.be.seamless.repository.UserRepository;
import team1.be.seamless.util.errorException.BaseHandler;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;
    private final ProjectOptionRepository projectOptionRepository;
    private final ProjectMapper projectMapper;
    private final OptionMapper optionMapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository,
                          ProjectOptionRepository projectOptionRepository,
                          OptionRepository optionRepository, ProjectMapper projectMapper, OptionMapper optionMapper) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.optionRepository = optionRepository;
        this.projectOptionRepository = projectOptionRepository;
        this.projectMapper = projectMapper;
        this.optionMapper = optionMapper;
    }

    /**
     * @param param : 페이지네이션에 관한 parameter
     * @param email : 유저 토큰에서 추출한 email 정보
     * @return : 페이지네이션된 프로젝트들에 대한 정보 ProjectEntity에 매핑된 UserEntity의 email 정보가 일치하고 isDeleted가
     * false인 프로젝트를 페이지네이션 형식으로 반환
     */
    public Page<ProjectDetail> getProjectList(ProjectDTO.getList param, String email, String role) {
        validateRole(role);

        return projectRepository.findAllByUserEntityEmailAndIsDeletedFalse(param.toPageable(), email).map(projectMapper::toDetail);

    }

    /**
     * @param projectId : 프로젝트 Id
     * @return : 해당 Id의 프로젝트의 정보를 반환 repository 조회시 존재 하지 않을 경우 Throw Not Found
     */
    public ProjectDetail getProject(long projectId, String email, String role) {
        ProjectEntity projectEntity = projectRepository.findByIdAndIsDeletedFalse(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));

        return projectMapper.toDetail(projectEntity);
    }

    /**
     * @param projectId : 프로젝트 Id
     * @return : 해당 id를 가진 프로젝트에 설정된 옵션 목록
     */
    public List<OptionDetail> getProjectOptions(long projectId, String email, String role) {
        validateRole(role);

        ProjectEntity projectEntity = projectRepository.findByIdAndIsDeletedFalse(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));

        validateProjectOwner(projectEntity, email);

        return projectEntity.getProjectOptions().stream()
                .map(projectOption -> optionMapper.toDetail(projectOption.getOptionEntity())).toList();
    }

    /**
     * @param param : 페이지네이션에 관한 parameter
     * @param email : 유저 토큰에서 추출한 email 정보
     * @return : 프로젝트의 Id, name, startDate, endDate 정보를 페이지네이션
     */
    public Page<ProjectDate> getProjectDate(ProjectDTO.getList param, String email, String role) {
        validateRole(role);

        return projectRepository.findAllByUserEntityEmailAndIsDeletedFalse(param.toPageable(), email).map(projectMapper::toDate);
    }

    /**
     * @param create : 프로젝트 생성 시 필요한 정보를 담은 DTO
     * @param email  : 유저 토큰에서 추출한 email 정보
     * @return : 생성한 프로젝트의 정보 플로우 : email을 통해 유저가 존재하는 지 검증 -> DTO에 담긴 optionEntity들의 id 정보들을 통해
     * OptionEntity조회 -> OptionEntity을 ProjectOption으로 매핑 -> 해당 정보를 가진 ProjectEntity를 생성 후 Repo에
     * save -> 각 ProjectOption의 ProjectEntity field를 생성한 ProjectEntity로 설정
     */
    @Transactional
    public ProjectDetail createProject(ProjectCreate create, String email, String role) {
        validateRole(role);


        UserEntity userEntity = userRepository.findByEmailAndIsDeleteFalse(email)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "사용자가 존재하지 않음"));


        create.getOptionIds()
                .forEach(id -> optionRepository.findById(id)
                        .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 옵션이 존재하지 않음")));

        List<OptionEntity> optionEntities = optionRepository.findByIdIn(create.getOptionIds());

        List<ProjectOptionEntity> projectOptionEntities = optionEntities.stream().map(ProjectOptionEntity::new).toList();

        ProjectEntity projectEntity = projectRepository.save(projectMapper.toEntity(create, userEntity, projectOptionEntities));

        projectOptionEntities.forEach(option -> option.setProjectEntity(projectEntity));

        return projectMapper.toDetail(projectEntity);
    }

    /**
     * @param projectId : 프로젝트 Id
     * @param update    : 프로젝트 업데이트 시 필요한 정보를 담은 DTO
     * @return : 수정한 프로젝트의 정보 플로우 : 프로젝트가 존재하는지 검증 -> 기존의 ProjectOptionEntity 삭제 -> DTO에 담긴 Option
     * id들을 통해 OptionEntity 조회 -> OptionEntity를 통해 새 ProjectOption 생성 -> 업데이트
     */
    @Transactional
    public ProjectDetail updateProject(long projectId, ProjectUpdate update, String email, String role) {

        validateRole(role);

        ProjectEntity projectEntity = projectRepository.findByIdAndIsDeletedFalse(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));

        validateProjectOwner(projectEntity, email);


        projectOptionRepository.deleteByProjectEntity(projectEntity);


        update.getOptionIds()
                .forEach(id -> optionRepository.findById(id).
                        orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 옵션이 존재하지 않음")));


        List<OptionEntity> optionEntities = optionRepository.findByIdIn(update.getOptionIds());
        List<ProjectOptionEntity> newProjectOptionEntities = new ArrayList<>();

        for (OptionEntity optionEntity : optionEntities) {
            ProjectOptionEntity projectOptionEntity = new ProjectOptionEntity(projectEntity, optionEntity);
            newProjectOptionEntities.add(projectOptionEntity);
        }


        projectEntity.getTaskEntities().stream()
                .filter(taskEntity -> taskEntity.getIsDeleted().equals(false))
                .map(TaskEntity::getEndDate)
                .max(Comparator.naturalOrder())
                .ifPresent(lastEndDate -> {
                    if (lastEndDate.isAfter(update.getEndDate())) {
                        throw new BaseHandler(HttpStatus.BAD_REQUEST,
                                "변경하려는 프로젝트의 종료일은 현재 태스크의 가장 늦은 종료일 보다 이릅니다.");
                    }
                });

        projectMapper.toUpdate(projectEntity, update, newProjectOptionEntities);

        return projectMapper.toDetail(projectEntity);
    }

    /**
     * @param projectId : 프로젝트 Id
     * @return : 삭제한 프로젝트의 Id 프로젝트의 존재 검증 후 존재 시 삭제
     */
    @Transactional
    public Long deleteProject(long projectId, String email, String role) {

        validateRole(role);

        ProjectEntity projectEntity = projectRepository.findByIdAndIsDeletedFalse(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));

        validateProjectOwner(projectEntity, email);

        projectEntity.setIsDeleted(true);
        return projectEntity.getId();
    }

    private void validateRole(String role) {
        if (!Role.USER.isRole(role)) {
            throw new BaseHandler(HttpStatus.FORBIDDEN, "로그인한 유저만 권한이 존재 합니다.");
        }
    }

    private void validateProjectOwner(ProjectEntity projectEntity, String email) {
        if (!projectEntity.getUserEntity().getEmail().equals(email)) {
            throw new BaseHandler(HttpStatus.FORBIDDEN, "해당 프로젝트의 관라자만 권한이 존재 합니다.");
        }
    }

}