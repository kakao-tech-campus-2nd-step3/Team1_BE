package team1.BE.seamless.DTO;

import java.util.List;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.TaskEntity;

public class MemberDetailResponseDTO {

    private String email;
    private String role;
    private String name;
    private String imageURL;
    private ProjectEntity projectEntity;
    private List<TaskEntity> taskEntities;

    public MemberDetailResponseDTO(String email, String role, String name,
        String imageURL, ProjectEntity projectEntity, List<TaskEntity> taskEntities) {
        this.email = email;
        this.role = role;
        this.name = name;
        this.imageURL = imageURL;
        this.projectEntity = projectEntity;
        this.taskEntities = taskEntities;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectEntity getProjectEntity() {
        return projectEntity;
    }

    public void setProjectEntity(ProjectEntity projectEntity) {
        this.projectEntity = projectEntity;
    }

    public List<TaskEntity> getTaskEntities() {
        return taskEntities;
    }

    public void setTaskEntities(List<TaskEntity> taskEntities) {
        this.taskEntities = taskEntities;
    }
}
