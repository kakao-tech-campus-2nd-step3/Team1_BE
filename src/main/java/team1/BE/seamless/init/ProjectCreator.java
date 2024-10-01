package team1.BE.seamless.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team1.BE.seamless.service.ProjectService;

@Component
public class ProjectCreator {

    private final ProjectService projectService;

    @Autowired
    public ProjectCreator(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void creator() {
    }
}
