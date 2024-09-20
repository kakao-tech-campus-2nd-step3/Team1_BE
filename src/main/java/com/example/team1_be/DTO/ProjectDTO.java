package com.example.team1_be.DTO;

import com.example.team1_be.entity.Guest;
import com.example.team1_be.entity.ProjectOption;
import com.example.team1_be.entity.User;
import com.example.team1_be.util.page.PageParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProjectDTO {

    public static class getList extends PageParam {

    }

    public static class create {

        private String name;

        private Object viewType;

        private Integer isDelete;

        private User user;

        private List<Guest> guests;

        private List<ProjectOption> options;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public create() {
        }

        public create(String name, Object viewType, Integer isDelete, User user,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<Guest> guests,
            List<ProjectOption> options) {
            this.name = name;
            this.viewType = viewType;
            this.isDelete = isDelete;
            this.user = user;
            this.startDate = startDate;
            this.endDate = endDate;

            if (guests == null) {
                this.guests = new ArrayList<>();
            } else {
                this.guests = guests;
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

        public Object getViewType() {
            return viewType;
        }

        public Integer getIsDelete() {
            return isDelete;
        }

        public User getUser() {
            return user;
        }

        public List<Guest> getGuests() {
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

    public static class update {

        private String name;

        private Object viewType;

        private User user;

        private List<Guest> guests;

        private List<ProjectOption> options;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public update() {
        }

        public update(String name, Object viewType, User user,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<Guest> guests,
            List<ProjectOption> options) {
            this.name = name;
            this.viewType = viewType;
            this.user = user;
            this.startDate = startDate;
            this.endDate = endDate;

            if (guests == null) {
                this.guests = new ArrayList<>();
            } else {
                this.guests = guests;
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

        public Object getViewType() {
            return viewType;
        }

        public User getUser() {
            return user;
        }

        public List<Guest> getGuests() {
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

