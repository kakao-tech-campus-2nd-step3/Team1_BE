package team1.BE.seamless.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import team1.BE.seamless.util.page.PageParam;

public class MemberRequestDTO {
    public static class getMemberList extends PageParam {

    }

    public static class CreateMember {
        @NotBlank(message = "이름은 필수 입력 사항입니다.")
        @Size(max = 15, message = "이름은 공백 포함 최대 15글자까지 가능합니다.")
        private String name;

        @NotBlank(message = "역할은 필수 입력 사항입니다.")
        @Size(max = 15, message = "역할은 공백 포함 최대 15글자까지 가능합니다.")
        private String role;

        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        private String email;

        private String imageURL;

        public CreateMember() {
        }

        public CreateMember(String name, String role, String email, String imageURL) {
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
