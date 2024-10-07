package team1.BE.seamless.DTO;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import team1.BE.seamless.util.page.PageParam;

public class ProjectDTO {

    public static class getList extends PageParam {

    }

    public static class ProjectCreate {

        private String name;

        private Long userId;

        private List<Long> optionIds;

        @NotNull
        @FutureOrPresent
        private LocalDateTime startDate;

        @NotNull
        private LocalDateTime endDate;

        public ProjectCreate() {
            this.optionIds = new ArrayList<>();
        }

        public ProjectCreate(String name, Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<Long> optionIds) {
            this.name = name;
            this.userId = userId;
            this.startDate = startDate;
            this.endDate = endDate;
            this.optionIds = optionIds;
        }

        public String getName() {
            return name;
        }

        public Long getUserId() {
            return userId;
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

        private String name;

        private List<Long> optionIds;

        @NotNull
        @FutureOrPresent
        private LocalDateTime startDate;

        @NotNull
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

        @NotNull
        @FutureOrPresent
        private LocalDateTime startDate;

        @NotNull
        private LocalDateTime endDate;

        public ProjectPeriod() {
        }

        public ProjectPeriod(String name, LocalDateTime startDate, LocalDateTime endDate) {
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

        @AssertTrue
        public boolean isEndDateAfterStartDate() {
            return endDate.isAfter(startDate);
        }

        @AssertTrue
        public boolean isAtLeastOneDayDifference() {
            return Duration.between(startDate, endDate).toDays() >= 1;
        }

    }

}

