package team1.BE.seamless.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class TaskDTO {

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String name;

    private String remark;

    @Min(value = 0, message = "진행률의 최솟값은 0입니다.")
    @Max(value = 100, message = "진행률의 최댓값은 100입니다.")
    private Integer progress;

    @Min(value = 0, message = "삭제 여부는 0 또는 1입니다.")
    @Max(value = 1, message = "삭제 여부는 0 또는 1입니다.")
    private Boolean isDeleted;

    @NotNull(message = "프로젝트 아아디는 필수 입력 사항입니다.")
    private Long projectId;

    @NotNull(message = "프로젝트 담당자 아이디는 필수 입력 사항입니다.")
    private Long ownerId; // 프로젝트 담당자

    @NotNull(message = "시작 시간은 필수 입력 사항입니다.")
    private LocalDateTime startDate;

    @NotNull(message = "종료 시간은 필수 입력 사항입니다.")
    private LocalDateTime endDate;

    public TaskDTO(String name, String remark, Integer progress, Boolean isDeleted, Long projectId,
        Long ownerId, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.remark = remark;
        this.progress = progress;
        this.isDeleted = isDeleted;
        this.projectId = projectId;
        this.ownerId = ownerId;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public Long getProjectId() {
        return projectId;
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
}
