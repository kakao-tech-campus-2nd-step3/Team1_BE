package team1.BE.seamless.DTO;

public class MemberResponseDTO {

    private String message;

    private String name;

    private String role;

    private String email;

    private String attendURL;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MemberResponseDTO(String message, String name, String role, String email) {
        this.message = message;
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public MemberResponseDTO(String message, String name, String role, String email, String attendURL, Long id) {
        this.message = message;
        this.name = name;
        this.role = role;
        this.email = email;
        this.id = id;
        this.attendURL = attendURL;
    }

    public MemberResponseDTO(String message, String name, String role, String email, Long id) {
        this.message = message;
        this.name = name;
        this.role = role;
        this.email = email;
        this.id = id;
    }

    public MemberResponseDTO(String message, String name, String role, String email, String attendURL) {
        this.message = message;
        this.name = name;
        this.role = role;
        this.email = email;
        this.attendURL = attendURL;
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

    public String getattendURL() {
        return attendURL;
    }
}
