package team1.BE.seamless.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team1.BE.seamless.DTO.ProjectDTO;
import team1.BE.seamless.DTO.ProjectDTO.ProjectCreate;
import team1.BE.seamless.DTO.ProjectDTO.ProjectPeriod;
import team1.BE.seamless.DTO.ProjectDTO.ProjectUpdate;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.ProjectOption;
import team1.BE.seamless.entity.UserEntity;
import team1.BE.seamless.mapper.ProjectMapper;
import team1.BE.seamless.repository.ProjectRepository;
import team1.BE.seamless.repository.UserRepository;
import team1.BE.seamless.util.errorException.BaseHandler;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMapper = projectMapper;
    }

    public Page<ProjectEntity> getProjectList(ProjectDTO.getList param, String email) {
        return projectRepository.findAllByUserEntityEmail(param.toPageable(), email);
    }

    public ProjectEntity getProject(long id, String email) {
        return projectRepository.findByIdAndUserEntityEmail(id, email)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));
    }

    public List<MemberEntity> getProjectMembers(long id, String email) {
        ProjectEntity projectEntity = projectRepository.findByIdAndUserEntityEmail(id, email)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));
        return projectEntity.getMemberEntities();
    }

    public Page<ProjectPeriod> getProjectPeriod(ProjectDTO.getList param, String email) {
        return projectRepository.findByUserEntityEmail(param.toPageable(), email);
    }

    @Transactional
    public ProjectEntity createProject(ProjectCreate create, String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "사용자가 존재하지 않음"));
        return projectRepository.save(projectMapper.toEntity(create,userEntity));
    }

    @Transactional
    public ProjectEntity updateProject(long get, ProjectUpdate update) {
        ProjectEntity projectEntity = projectRepository.findById(get)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));

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