package com.example.team1_be.DTO;

import java.time.LocalDateTime;

public class UserDTO {

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
