package com.example.team1_be.service;

import com.example.team1_be.DTO.ProjectDTO;
import com.example.team1_be.entity.Guest;
import com.example.team1_be.entity.Project;
import com.example.team1_be.entity.ProjectOption;
import com.example.team1_be.repository.ProjectRepository;
import com.example.team1_be.util.errorException.BaseHandler;
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

    public Page<Project> getProjectList(ProjectDTO.getList param) {
        return projectRepository.findAll(param.toPageable());
    }

    public Project getProject(long get) {
        Project project = projectRepository.findById(get)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않음"));
        return project;
    }

    public List<Guest> getProjectMembers(long get) {
        Project project = projectRepository.findById(get)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않음"));
        return project.getGuests();
    }

    public Project createProject(ProjectDTO.create create) {
        Project project = new Project(
            create.getName(),
            create.getIsDelete(),
            create.getUser(),
            create.getStartDate(),
            create.getEndDate()
        );

        if (create.getGuests() != null) {
            for (Guest guest : create.getGuests()) {
                project.addGuest(guest);
            }
        }

        if (create.getOptions() != null) {
            for (ProjectOption option : create.getOptions()) {
                project.addOption(option);
            }
        }
        projectRepository.save(project);
        return project;
    }

    public Project updateProject(long get, ProjectDTO.update update) {
        Project project = projectRepository.findById(get)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않음"));

        project.setName(update.getName());
        project.setUser(update.getUser());
        project.setGuests(update.getGuests());
        project.setOptions(update.getOptions());
        project.setStartDate(update.getStartDate());
        project.setEndDate(update.getEndDate());

        projectRepository.save(project);
        return project;
    }

    public void deleteProject(long get) {
        projectRepository.deleteById(get);
    }

}
