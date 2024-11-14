package team1.be.seamless.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.validator.constraints.URL;
import team1.be.seamless.util.page.PageParam;

public class ProjectDTO {

    public static class getList extends PageParam {

    }

    public static class ProjectCreate {

        @NotBlank(message = "이름은 필수 입력 사항입니다.")
        @Size(max = 15, message = "이름은 공백 포함 최대 15글자까지 가능합니다.")
        private String name;

        @Size(max = 50, message = "설명을 50글자까지 가능합니다.")
        private String description;

        @URL(message = "URL 형식으로 입력해주세요.")
        @Pattern(regexp = "^https?://.*\\.(jpg|jpeg|png|gif|bmp|webp)$",
            message = "이미지 URL은 .jpg, .jpeg, .png, .gif, .bmp, .webp 형식이어야 합니다.")
        private String imageURL;

        @NotNull
        @Valid
        private List<@Positive Long> optionIds = new ArrayList<>();

        @NotNull
        private LocalDateTime startDate;

        @NotNull
        private LocalDateTime endDate;

        public ProjectCreate() {
        }

        public ProjectCreate(
            String name,
            String description,
            String imageURL,
            List<Long> optionIds,
            LocalDateTime startDate,
            LocalDateTime endDate) {
            this.name = name;
            this.description = description;
            this.imageURL = imageURL;
            this.optionIds = optionIds;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getImageURL() {
            return imageURL;
        }

        public List<Long> getOptionIds() {
            return optionIds;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        @AssertTrue
        public boolean isEndDateAfterStartDate() {
            return endDate.isAfter(startDate);
        }

        @AssertTrue
        public boolean isAtLeastOneDayDifference() {
            return Duration.between(startDate, endDate).toDays() >= 1;
        }

    }

    public static class ProjectUpdate {

        @NotBlank(message = "이름은 필수 입력 사항입니다.")
        @Size(max = 15, message = "이름은 공백 포함 최대 15글자까지 가능합니다.")
        private String name;

        @Size(max = 50, message = "설명을 50글자까지 가능합니다.")
        private String description;

        @URL(message = "URL 형식으로 입력해주세요.")
        @Pattern(regexp = "^https?://.*\\.(jpg|jpeg|png|gif|bmp|webp)$",
            message = "이미지 URL은 .jpg, .jpeg, .png, .gif, .bmp, .webp 형식이어야 합니다.")
        private String imageURL;

        @NotNull
        @Valid
        private List<@Positive Long> optionIds = new ArrayList<>();

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public ProjectUpdate() {
        }

        public ProjectUpdate(
            String name,
            String description,
            String imageURL,
            List<Long> optionIds,
            LocalDateTime startDate,
            LocalDateTime endDate) {
            this.name = name;
            this.description = description;
            this.imageURL = imageURL;
            this.optionIds = optionIds;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getImageURL() {
            return imageURL;
        }

        public List<Long> getOptionIds() {
            return optionIds;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        @AssertTrue
        public boolean isEndDateAfterStartDate() {
            return endDate.isAfter(startDate);
        }

        @AssertTrue
        public boolean isAtLeastOneDayDifference() {
            return Duration.between(startDate, endDate).toDays() >= 1;
        }

    }

    public static class ProjectDate {

        private Long id;

        private String name;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public ProjectDate() {
        }

        public ProjectDate(Long id, String name, LocalDateTime startDate, LocalDateTime endDate) {
            this.id = id;
            this.name = name;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

    }

    public static class ProjectDetail {

        private Long id;

        private String name;

        private String description;

        private String imageURL;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        private List<Long> optionIds;

        private int totalMembers;

        private ProjectManager projectManager;

        public ProjectDetail() {
        }


        public ProjectDetail(
            Long id,
            String name,
            String description,
            String imageURL,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<Long> optionIds,
            int totalMembers,
            ProjectManager projectManager
        ) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.imageURL = imageURL;
            this.startDate = startDate;
            this.endDate = endDate;
            this.optionIds = optionIds;
            this.totalMembers = totalMembers;
            this.projectManager = projectManager;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getImageURL() {
            return imageURL;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public List<Long> getOptionIds() {
            return optionIds;
        }

        public int getTotalMembers() {
            return totalMembers;
        }

        public ProjectManager getProjectManager() {
            return projectManager;
        }
    }

    public static class ProjectManager {

        private String name;
        private String imageURL;

        public ProjectManager(String name, String imageURL) {
            this.name = name;
            this.imageURL = imageURL;
        }

        public String getName() {
            return name;
        }

        public String getImageURL() {
            return imageURL;
        }
    }
}

