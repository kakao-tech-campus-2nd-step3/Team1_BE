package team1.BE.seamless.DTO;

public class MemberResponseDTO {
    private String message;

    public MemberResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
