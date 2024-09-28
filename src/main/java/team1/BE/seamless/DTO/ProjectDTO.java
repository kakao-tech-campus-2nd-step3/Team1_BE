package team1.BE.seamless.DTO;

import team1.BE.seamless.util.page.PageParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProjectDTO {

    public static class getList extends PageParam {

    }

    public static class ProjectCreate {

        private String name;

        private Long userId;

        private List<Long> optionIds;

        private LocalDateTime startDate;

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

    }

    public static class ProjectUpdate {

        private String name;

        private List<Long> optionIds;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public ProjectUpdate() {
        }

        public ProjectUpdate(String name, List<Long> optionIds, LocalDateTime startDate, LocalDateTime endDate) {
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

    }

    public interface ProjectPeriod {
        Long getId();
        String getName();
        LocalDateTime getStartDate();
        LocalDateTime getEndDate();
    }

}

