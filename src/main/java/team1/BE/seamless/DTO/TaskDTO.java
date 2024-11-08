package team1.BE.seamless.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.TaskEntity;
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

        public TaskCreate() {
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

        public TaskDetail(Long id, String name, String description, Long ownerId, Integer progress,
            LocalDateTime startDate, LocalDateTime endDate, Priority priority,
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

        public TaskDetail(TaskEntity task) {
            this.id = task.getId();
            this.name = task.getName();
            this.description = task.getDescription();
            this.ownerId = task.getId();
            this.progress = task.getProgress();
            this.description = task.getDescription();
            this.startDate = task.getStartDate();
            this.endDate = task.getEndDate();
            this.status = task.getStatus();
            this.priority = task.getPriority();
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

    public static class TaskWithOwnerDetail {

        private Long id;

        private String name;

        private String description;

        private OwnerDetail owner;

        private Integer progress;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        private Priority priority;

        private Integer status;

        public TaskWithOwnerDetail(Long id, String name, String description, MemberEntity owner,
            Integer progress,
            LocalDateTime startDate, LocalDateTime endDate, Priority priority, Integer status) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.owner = new OwnerDetail(owner);
            this.progress = progress;
            this.startDate = startDate;
            this.endDate = endDate;
            this.priority = priority;
            this.status = status;
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

        public OwnerDetail getOwner() {
            return owner;
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

        public Priority getPriority() {
            return priority;
        }

        public Integer getStatus() {
            return status;
        }
    }

    public static class OwnerDetail {

        private Long id;

        private String name;

        private String role;

        private String imageURL;

        public OwnerDetail(MemberEntity owner) {
            this.id = owner.getId();
            this.name = owner.getName();
            this.role = owner.getRole();
            this.imageURL = owner.getImageURL();
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }

        public String getImageURL() {
            return imageURL;
        }
    }
    public static class ProjectProgress {

        private Long projectId;

        private Integer projectProgress;

        private String treeGrowthStage;

        private String description;

        public ProjectProgress(Long projectId, Integer projectProgress, String treeGrowthStage,
            String description) {
            this.projectId = projectId;
            this.projectProgress = projectProgress;
            this.treeGrowthStage = treeGrowthStage;
            this.description = description;
        }

        public Long getProjectId() {
            return projectId;
        }

        public Integer getProjectProgress() {
            return projectProgress;
        }

        public String getTreeGrowthStage() {
            return treeGrowthStage;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class MemberProgress {
        private OwnerDetail teamMember;

        private Integer progress;

        private List<TaskDetail> activeTasks;

        public MemberProgress(MemberEntity teamMember, Integer progress, List<TaskEntity> activeTasks) {
            this.teamMember = new OwnerDetail(teamMember);
            this.progress = progress;
            this.activeTasks = activeTasks.stream().map(TaskDetail::new).collect(Collectors.toList());
        }

        public OwnerDetail getTeamMember() {
            return teamMember;
        }

        public Integer getProgress() {
            return progress;
        }

        public List<TaskDetail> getActiveTasks() {
            return activeTasks;
        }
    }
}
