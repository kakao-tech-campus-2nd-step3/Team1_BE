package team1.BE.seamless.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import team1.BE.seamless.entity.enums.Priority;
import team1.BE.seamless.util.errorException.BaseHandler;
import team1.BE.seamless.util.page.PageParam;

public class TaskDTO {

    public static class getList extends PageParam {

    }

    public static class TaskCreate {

        @NotBlank(message = "이름은 필수 입력 사항입니다.")
        private String name;

        private String description;

        @NotNull(message = "멤버 아이디는 필수 입력 사항입니다.")
        private Long ownerId;

        @NotNull(message = "진행 상태(status)는 필수 입력 사항입니다.")
        private Integer status;

        @NotNull(message = "중요도(priority)는 필수 입력 사항입니다.")
        private Priority priority;

        @NotNull(message = "진행도는 필수 입력 사항입니다.")
        @Min(value = 0, message = "진행도(progress)는 최소 0이어야 합니다.")
        @Max(value = 100, message = "진행도(progress)는 최대 100이어야 합니다.")
        private Integer progress;

        @NotNull(message = "시작 시간은 필수 입력 사항입니다.")
        private LocalDateTime startDate;

        @NotNull(message = "종료 시간은 필수 입력 사항입니다.")
        private LocalDateTime endDate;

        public TaskCreate(String name, String description, Long ownerId, LocalDateTime startDate,
            LocalDateTime endDate, Priority priority, Integer status, Integer progress) {
            if (endDate.isBefore(startDate)) {
                throw new BaseHandler(HttpStatus.BAD_REQUEST, "종료시간은 시작시간보다 이전일 수 없습니다.");
            }
            this.name = name;
            this.description = description;
            this.ownerId = ownerId;
            this.startDate = startDate;
            this.endDate = endDate;
            this.priority = priority;
            this.status = status;
            this.progress = progress;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Long getOwnerId() {
            return ownerId;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public Integer getProgress() {
            return progress;
        }

        public Priority getPriority() {
            return priority;
        }

        public Integer getStatus() {
            return status;
        }
    }

    public static class TaskUpdate {

        private String name;

        private String description;

        private Long ownerId;

        @Min(value = 0, message = "진행도(progress)는 최소 0이어야 합니다.")
        @Max(value = 100, message = "진행도(progress)는 최대 100이어야 합니다.")
        private Integer progress;

        private Integer status;

        private Priority priority;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public TaskUpdate(String name, String description, Integer progress, Long ownerId,
            LocalDateTime startDate,
            LocalDateTime endDate, Priority priority, Integer status) {
            if (endDate.isBefore(startDate)) {
                throw new BaseHandler(HttpStatus.BAD_REQUEST, "종료시간은 시작시간보다 이전일 수 없습니다.");
            }
            this.name = name;
            this.description = description;
            this.ownerId = ownerId;
            this.startDate = startDate;
            this.endDate = endDate;
            this.priority = priority;
            this.status = status;
            this.progress = progress;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Integer getProgress() {
            return progress;
        }

        public Long getOwnerId() {
            return ownerId;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public Integer getStatus() {
            return status;
        }

        public Priority getPriority() {
            return priority;
        }
    }

    public static class TaskDetail {

        private Long id;

        private String name;

        private String description;

        private Long ownerId;

        private Integer progress;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        private Priority priority;

        private Integer status;

        public TaskDetail(Long id, String name, String description, Long ownerId, Integer progress, LocalDateTime startDate, LocalDateTime endDate, Priority priority,
            Integer status) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.ownerId = ownerId;
            this.progress = progress;
            this.startDate = startDate;
            this.endDate = endDate;
            this.status = status;
            this.priority = priority;
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

        public Integer getProgress() {
            return progress;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public Long getOwnerId() {
            return ownerId;
        }

        public Priority getPriority() {
            return priority;
        }

        public Integer getStatus() {
            return status;
        }
    }
}
