package team1.BE.seamless.DTO;

// 팀원이 초대링크에 해당하는 페이지에서 이름, 이메일을 작성하여
// 요청을 보낼 때의 DTO
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
