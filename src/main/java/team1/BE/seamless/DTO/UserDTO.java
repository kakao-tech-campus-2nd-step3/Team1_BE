package team1.BE.seamless.DTO;

import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.URL;

public class UserDTO {

    public static class UserUpdate {

        private String username;

        @URL(protocol = "https", message = "사진은 URL이여야 합니다.")
        private String picture;

        public UserUpdate() {
        }

        public UserUpdate(String username, String picture) {
            this.username = username;
            this.picture = picture;
        }

        public String getUsername() {
            return username;
        }

        public String getPicture() {
            return picture;
        }
    }

    public static class UserSimple {

        private String username;
        private String email;
        private String picture;

        public UserSimple() {
        }

        public UserSimple(String username, String email, String picture) {
            this.username = username;
            this.email = email;
            this.picture = picture;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getPicture() {
            return picture;
        }
    }

    public static class UserDetails {

        private String username;
        private String email;
        private String picture;
        private String role;
        private LocalDateTime createDate;

        public UserDetails() {
        }

        public UserDetails(String username, String email, String picture, String role,
            LocalDateTime createDate) {
            this.username = username;
            this.email = email;
            this.picture = picture;
            this.role = role;
            this.createDate = createDate;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getPicture() {
            return picture;
        }

        public String getRole() {
            return role;
        }

        public LocalDateTime getCreateDate() {
            return createDate;
        }
    }

}
