package team1.BE.seamless.service;

import team1.BE.seamless.DTO.ProjectDTO;
import team1.BE.seamless.entity.GuestEntity;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.ProjectOption;
import team1.BE.seamless.repository.ProjectRepository;
import team1.BE.seamless.util.errorException.BaseHandler;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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

    public ProjectEntity createProject(ProjectDTO.create create) {
        ProjectEntity projectEntity = new ProjectEntity(
            create.getName(),
            create.getIsDelete(),
            create.getUser(),
            create.getStartDate(),
            create.getEndDate()
        );

        if (create.getGuests() != null) {
            for (GuestEntity guestEntity : create.getGuests()) {
                projectEntity.addGuest(guestEntity);
            }
        }

        if (create.getOptions() != null) {
            for (ProjectOption option : create.getOptions()) {
                projectEntity.addOption(option);
            }
        }
        projectRepository.save(projectEntity);
        return projectEntity;
    }

    public ProjectEntity updateProject(long get, ProjectDTO.update update) {
        ProjectEntity projectEntity = projectRepository.findById(get)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않음"));

        projectEntity.setName(update.getName());
        projectEntity.setUser(update.getUser());
        projectEntity.setGuests(update.getGuests());
        projectEntity.setOptions(update.getOptions());
        projectEntity.setStartDate(update.getStartDate());
        projectEntity.setEndDate(update.getEndDate());

        projectRepository.save(projectEntity);
        return projectEntity;
    }

    public Long deleteProject(long get) {
        projectRepository.deleteById(get);
        return get;
    }

}
