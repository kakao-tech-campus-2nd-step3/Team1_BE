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

    public Page<ProjectEntity> getProjectList(ProjectDTO.getList param, String email) {
        return projectRepository.findAllByUserEntityEmailAndIsDeletedFalse(param.toPageable(),
            email);
    }

    public ProjectEntity getProject(long id) {
        return projectRepository.findById(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));
    }

    public List<MemberEntity> getProjectMembers(long id) {
        ProjectEntity projectEntity = projectRepository.findById(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));
        return projectEntity.getMemberEntities();
    }

    public Page<ProjectPeriod> getProjectPeriod(ProjectDTO.getList param, String email) {
        return projectRepository.findByUserEntityEmailAndIsDeletedFalse(param.toPageable(), email);
    }

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

    @Transactional
    public ProjectEntity updateProject(long get, ProjectUpdate update) {
        ProjectEntity projectEntity = projectRepository.findById(get)
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

    @Transactional
    public Long deleteProject(long get) {
        ProjectEntity projectEntity = projectRepository.findById(get)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));
        projectRepository.deleteById(projectEntity.getId());
        return projectEntity.getId();
    }

}