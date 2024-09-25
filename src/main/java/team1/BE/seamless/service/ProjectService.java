package team1.BE.seamless.service;

import org.springframework.beans.factory.annotation.Autowired;
import team1.BE.seamless.DTO.ProjectDTO;
import team1.BE.seamless.DTO.ProjectDTO.ProjectCreate;
import team1.BE.seamless.DTO.ProjectDTO.ProjectPeriod;
import team1.BE.seamless.DTO.ProjectDTO.ProjectUpdate;
import team1.BE.seamless.entity.GuestEntity;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.ProjectOption;
import team1.BE.seamless.mapper.ProjectMapper;
import team1.BE.seamless.repository.ProjectRepository;
import team1.BE.seamless.util.errorException.BaseHandler;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    public Page<ProjectEntity> getProjectList(ProjectDTO.getList param) {
        return projectRepository.findAll(param.toPageable());
    }

    public ProjectEntity getProject(long get) {
        ProjectEntity projectEntity = projectRepository.findById(get)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않음"));
        return projectEntity;
    }

    public List<GuestEntity> getProjectMembers(long get) {
        ProjectEntity projectEntity = projectRepository.findById(get)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않음"));
        return projectEntity.getGuests();
    }

    public Page<ProjectPeriod> getProjectPeriod(ProjectDTO.getList param) {
        return projectRepository.findProjectPeriod(param.toPageable());
    }

    public ProjectEntity createProject(ProjectCreate create) {
        ProjectEntity projectEntity = projectMapper.toEntity(
            create.getName(),
            create.getUser(),
            create.getStartDate(),
            create.getEndDate()
        );

        projectRepository.save(projectEntity);
        return projectEntity;
    }

    public ProjectEntity updateProject(long get, ProjectUpdate update) {
        ProjectEntity projectEntity = projectRepository.findById(get)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않음"));

        projectEntity.update(
            update.getName(),
            update.getStartDate(),
            update.getEndDate()
        );

        projectRepository.save(projectEntity);
        return projectEntity;
    }

    public Long deleteProject(long get) {
        projectRepository.deleteById(get);
        return get;
    }

}
