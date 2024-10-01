package team1.BE.seamless.DTO;

import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.ProjectOption;
import team1.BE.seamless.entity.UserEntity;
import team1.BE.seamless.entity.UserEntity;
import team1.BE.seamless.util.page.PageParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProjectDTO {

    public static class getList extends PageParam {

    }

    public static class ProjectCreate {

        private String name;

        private UserEntity user;

        private List<MemberEntity> guests;

        private List<ProjectOption> options;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public ProjectCreate() {
            this.guests = new ArrayList<>();
            this.options = new ArrayList<>();
        }

        public ProjectCreate(String name, UserEntity user,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<MemberEntity> guests,
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

        public UserEntity getUser() {
            return user;
        }

        public List<MemberEntity> getGuests() {
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

        private List<MemberEntity> guests;

        private List<ProjectOption> options;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public ProjectUpdate() {
        }

        public ProjectUpdate(String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<MemberEntity> guests,
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

        public List<MemberEntity> getGuests() {
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

//    public static class ProjectPeriod {
//
//        private Long id;
//
//        private String name;
//
//        private LocalDateTime startDate;
//
//        private LocalDateTime endDate;
//
//        public ProjectPeriod() {
//        }
//
//        public ProjectPeriod(Long id,String name, LocalDateTime startDate, LocalDateTime endDate) {
//            this.id = id;
//            this.name = name;
//            this.startDate = startDate;
//            this.endDate = endDate;
//        }
//
//        public Long getId() {
//            return id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public LocalDateTime getStartDate() {
//            return startDate;
//        }
//
//        public LocalDateTime getEndDate() {
//            return endDate;
//        }
//
//    }

    public interface ProjectPeriod {
        Long getId();
        String getName();
        LocalDateTime getStartDate();
        LocalDateTime getEndDate();
    }

}

