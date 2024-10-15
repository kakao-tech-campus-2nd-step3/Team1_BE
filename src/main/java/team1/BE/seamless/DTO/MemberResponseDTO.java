package team1.BE.seamless.DTO;

public class MemberResponseDTO {

    private String message;

    private String name;

    private String role;

    private String email;

    public MemberResponseDTO(String message, String name, String role, String email) {
        this.message = message;
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
