package team1.be.seamless.util.auth;

public class MemberToken {

    private String token;

    private String projectId;

    public MemberToken(String token, String projectId) {
        this.token = token;
        this.projectId = projectId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
