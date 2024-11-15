package team1.be.seamless.dto;


public class InviteRequestDTO {

    private Long projectId;
    private String email;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InviteRequestDTO() {
    }

    public InviteRequestDTO(Long projectId, String email, String name) {
        this.projectId = projectId;
        this.email = email;
        this.name = name;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
