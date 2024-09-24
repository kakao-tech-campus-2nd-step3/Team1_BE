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

    public static class create {

        private String name;

//        private Object viewType;

        private Integer isDelete;

        private User user;

        private List<GuestEntity> guestEntities;

        private List<ProjectOption> options;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public create() {
        }

        public create(String name, Integer isDelete, User user,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<GuestEntity> guestEntities,
            List<ProjectOption> options) {
            this.name = name;
            this.isDelete = isDelete;
            this.user = user;
            this.startDate = startDate;
            this.endDate = endDate;

            if (guestEntities == null) {
                this.guestEntities = new ArrayList<>();
            } else {
                this.guestEntities = guestEntities;
            }

            if (options == null) {
                this.options = new ArrayList<>();
            } else {
                this.options = options;
            }
        }

        public String getName() {
            return name;
        }

        public Integer getIsDelete() {
            return isDelete;
        }

        public User getUser() {
            return user;
        }

        public List<GuestEntity> getGuests() {
            return guestEntities;
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

    public static class update {

        private String name;

//        private Object viewType;

        private User user;

        private List<GuestEntity> guestEntities;

        private List<ProjectOption> options;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public update() {
        }

        public update(String name, User user,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<GuestEntity> guestEntities,
            List<ProjectOption> options) {
            this.name = name;
            this.user = user;
            this.startDate = startDate;
            this.endDate = endDate;

            if (guestEntities == null) {
                this.guestEntities = new ArrayList<>();
            } else {
                this.guestEntities = guestEntities;
            }

            if (options == null) {
                this.options = new ArrayList<>();
            } else {
                this.options = options;
            }
        }

        public String getName() {
            return name;
        }

        public User getUser() {
            return user;
        }

        public List<GuestEntity> getGuests() {
            return guestEntities;
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

