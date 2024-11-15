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
                        "테스트 프로젝트",
                        "카카오테크캠퍼스",
                        "https://ca.slack-edge.com/T06MHGBCEKG-U06M9KTL9GX-e92c9472326e-512",
                        List.of(1L, 2L, 3L),
                        LocalDateTime.of(2024, 4, 1, 0, 0, 0),
                        LocalDateTime.of(2025, 10, 1, 0, 0, 0)
                ), "kakaotechcampus.mentor@gmail.com", Role.USER.getKey());

    }
}
