package team1.BE.seamless.DTO;

import team1.BE.seamless.entity.GuestEntity;
import team1.BE.seamless.entity.ProjectOption;
import team1.BE.seamless.entity.User;
import team1.BE.seamless.util.page.PageParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProjectDTO {

    public static class getList extends PageParam {

    }

    public static class ProjectCreate {

        private String name;

        private User user;

        private List<GuestEntity> guests;

        private List<ProjectOption> options;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public ProjectCreate() {
            this.guests = new ArrayList<>();
            this.options = new ArrayList<>();
        }

        public ProjectCreate(String name, User user,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<GuestEntity> guests,
            List<ProjectOption> options) {
            this.name = name;
            this.user = user;
            this.startDate = startDate;
            this.endDate = endDate;
            this.guests = guests;
            this.options = options;
        }

        public String getName() {
            return name;
        }

        public User getUser() {
            return user;
        }

        public List<GuestEntity> getGuests() {
            return guests;
        }

        public List<ProjectOption> getOptions() {
            return options;
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

        private List<GuestEntity> guests;

        private List<ProjectOption> options;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public ProjectUpdate() {
        }

        public ProjectUpdate(String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<GuestEntity> guests,
            List<ProjectOption> options) {
            this.name = name;
            this.startDate = startDate;
            this.endDate = endDate;
            this.guests = guests;
            this.options = options;
        }

        public String getName() {
            return name;
        }

        public List<GuestEntity> getGuests() {
            return guests;
        }

        public List<ProjectOption> getOptions() {
            return options;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

    }

}

