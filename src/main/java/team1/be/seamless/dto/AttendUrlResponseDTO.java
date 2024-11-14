package team1.be.seamless.dto;

public class AttendUrlResponseDTO {

    String attendUrl;

    public AttendUrlResponseDTO() {
    }

    public AttendUrlResponseDTO(String attendUrl) {
        this.attendUrl = attendUrl;
    }

    public String getAttendUrl() {
        return attendUrl;
    }

    public void setAttendUrl(String attendUrl) {
        this.attendUrl = attendUrl;
    }
}
