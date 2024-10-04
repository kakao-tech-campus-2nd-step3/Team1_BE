package team1.BE.seamless.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import team1.BE.seamless.util.page.PageParam;

public class ProjectDTO {

    public static class getList extends PageParam {

    }

    public static class ProjectCreate {

        @NotBlank(message = "이름은 필수 입력 사항입니다.")
        @Size(max = 15, message = "이름은 공백 포함 최대 15글자까지 가능합니다.")
        private String name;

        @NotNull
        @Valid
        private List<@Positive Long> optionIds = new ArrayList<>();

        @NotNull
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")
        private LocalDateTime startDate;

        @NotNull
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")
        private LocalDateTime endDate;

        public ProjectCreate() {
        }

        public ProjectCreate(String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<Long> optionIds) {
            this.name = name;
            this.startDate = startDate;
            this.endDate = endDate;
            this.optionIds = optionIds;
        }

        public String getName() {
            return name;
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

        @NotNull
        @Valid
        private List<@Positive Long> optionIds = new ArrayList<>();

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")
        private LocalDateTime startDate;

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")
        private LocalDateTime endDate;

        public ProjectUpdate() {
        }

        public ProjectUpdate(String name, List<Long> optionIds, LocalDateTime startDate,
            LocalDateTime endDate) {
            this.name = name;
            this.optionIds = optionIds;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String getName() {
            return name;
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

    public static class ProjectPeriod {

        private Long id;

        private String name;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public ProjectPeriod() {
        }

        public ProjectPeriod(Long id, String name, LocalDateTime startDate, LocalDateTime endDate) {
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

}

