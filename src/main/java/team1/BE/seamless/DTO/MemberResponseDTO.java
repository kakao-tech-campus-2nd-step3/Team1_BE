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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
