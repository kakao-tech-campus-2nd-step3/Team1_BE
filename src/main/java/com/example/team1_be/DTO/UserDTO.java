package com.example.team1_be.DTO;

import java.time.LocalDateTime;

public class UserDTO {

    //    OAuth2에서 가져온 유저 정보
    public static class UserDetails {

        private String username;
        private String email;
        private String picture;
        private LocalDateTime createDate;

        public UserDetails() {
        }

        public UserDetails(String username, String email, String picture,
            LocalDateTime createDate) {
            this.username = username;
            this.email = email;
            this.picture = picture;
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

        public LocalDateTime getCreateDate() {
            return createDate;
        }
    }


}
