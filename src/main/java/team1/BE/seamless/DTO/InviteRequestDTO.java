package team1.BE.seamless.DTO;

// 팀원이 초대링크에 해당하는 페이지에서 이름, 이메일을 작성하여
// 요청을 보낼 때의 DTO
public class InviteRequestDTO {

    private Integer projectId;
    private String email;

    public InviteRequestDTO() {
    }

    public InviteRequestDTO(Integer projectId, String email) {
        this.projectId = projectId;
        this.email = email;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
