package team1.be.seamless.init;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team1.be.seamless.dto.ProjectDTO.ProjectCreate;
import team1.be.seamless.entity.enums.Role;
import team1.be.seamless.service.ProjectService;

@Component
public class ProjectCreator {

    private final ProjectService projectService;

    @Autowired
    public ProjectCreator(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void creator() {
        projectService.createProject(
            new ProjectCreate(
                "프로젝트 이름1",
                "프로젝트 설명1",
                "https://example.com/image1.jpg",
                List.of(1L, 2L, 3L),
                LocalDateTime.of(2024, 10, 1, 0, 0, 0),
                LocalDateTime.of(2025, 10, 1, 0, 0, 0)
            ), "user1@google.com", Role.USER.getKey());
    }
}
