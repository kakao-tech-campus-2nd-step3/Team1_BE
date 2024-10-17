package team1.BE.seamless.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import team1.BE.seamless.util.errorException.BaseHandler;
import team1.BE.seamless.util.page.PageParam;

public class TaskDTO {

    public static class getList extends PageParam {

    }

    public static class TaskCreate {

        @NotBlank(message = "이름은 필수 입력 사항입니다.")
        private String name;

        private String remark;

        private Long memberId;

        @NotNull(message = "시작 시간은 필수 입력 사항입니다.")
        private LocalDateTime startDate;

        @NotNull(message = "종료 시간은 필수 입력 사항입니다.")
        private LocalDateTime endDate;

        public TaskCreate(String name, String remark, Long memberId, LocalDateTime startDate,
            LocalDateTime endDate) {
            if (endDate.isBefore(startDate)) {
                throw new BaseHandler(HttpStatus.BAD_REQUEST, "종료시간은 시작시간보다 이전일 수 없습니다.");
            }
            this.name = name;
            this.remark = remark;
            this.memberId = memberId;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public @NotBlank(message = "이름은 필수 입력 사항입니다.") String getName() {
            return name;
        }

        public String getRemark() {
            return remark;
        }

        public Long getMemberId() {
            return memberId;
        }

        public @NotNull(message = "시작 시간은 필수 입력 사항입니다.") LocalDateTime getStartDate() {
            return startDate;
        }

        public @NotNull(message = "종료 시간은 필수 입력 사항입니다.") LocalDateTime getEndDate() {
            return endDate;
        }
    }

    public static class TaskUpdate {

        private String name;

        private String remark;

        private Integer progress;

        private Long memberId;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public TaskUpdate(String name, String remark, Integer progress, Long memberId,
            LocalDateTime startDate,
            LocalDateTime endDate) {
            if (endDate.isBefore(startDate)) {
                throw new BaseHandler(HttpStatus.BAD_REQUEST, "종료시간은 시작시간보다 이전일 수 없습니다.");
            }
            this.name = name;
            this.remark = remark;
            this.progress = progress;
            this.memberId = memberId;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String getName() {
            return name;
        }

        public String getRemark() {
            return remark;
        }

        public Integer getProgress() {
            return progress;
        }

        public Long getMemberId() {
            return memberId;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }
    }

    public static class TaskDetail {
        private Long id;

        private String name;

        private String remark;

        private Long memberId;

        private Integer progress;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public TaskDetail(Long id, String name, String remark, Long memberId, Integer progress,
            LocalDateTime startDate, LocalDateTime endDate) {
            this.id = id;
            this.name = name;
            this.remark = remark;
            this.memberId = memberId;
            this.progress = progress;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getRemark() {
            return remark;
        }

        public Long getMemberId() {
            return memberId;
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
    }
}
