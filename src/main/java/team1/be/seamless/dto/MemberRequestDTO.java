package team1.be.seamless.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import team1.be.seamless.util.page.PageParam;

public class MemberRequestDTO {

    public static class getMemberList extends PageParam {

    }

    public static class CreateMember {

        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        private String email;

        private String attendURL;

        private String name;

        public String getName() {
            return name;
        }

        public String getAttendURL() {
            return attendURL;
        }

        public CreateMember() {
        }

        public CreateMember(String email, String attendURL, String name) {
            this.email = email;
            this.attendURL = attendURL;
            this.name = name;
        }

        public @Email(message = "유효한 이메일 주소를 입력해주세요.") @NotBlank(message = "이메일은 필수 입력 사항입니다.") String getEmail() {
            return email;
        }

    }

    public static class UpdateMember {

        @Size(max = 15, message = "이름은 공백 포함 최대 15글자까지 가능합니다.")
        private String name;

        @Size(max = 15, message = "역할은 공백 포함 최대 15글자까지 가능합니다.")
        private String role;

        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        private String email;

        private String imageURL;

        public UpdateMember() {
        }

        public UpdateMember(String name, String role, String email, String imageURL, boolean test) {
            this.name = name;
            this.role = role;
            this.email = email;
            this.imageURL = imageURL;

        }

        public UpdateMember(String name, String role, String email, String imageURL) {
            this.name = name;
            this.role = role;
            this.email = email;
            this.imageURL = imageURL;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }

        public String getEmail() {
            return email;
        }

        public String getImageURL() {
            return imageURL;
        }
    }
}
