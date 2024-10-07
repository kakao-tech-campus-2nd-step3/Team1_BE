package team1.BE.seamless.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity(name = "task")
public class TaskEntity {

    public TaskEntity() {

    }

    public TaskEntity(String name, String remark, ProjectEntity projectEntity, MemberEntity owner,
        LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.remark = remark;
        this.progress = 0;
        this.isDeleted = false;
        this.projectEntity = projectEntity;
        this.owner = owner;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "remark")
    private String remark;

    @Column(name = "progress")
    private Integer progress = 0;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity owner;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    public Long getId() {
        return id;
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

    public ProjectEntity getProject() {
        return projectEntity;
    }

    public MemberEntity getOwner() {
        return owner;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setOwner(MemberEntity owner) {
        this.owner = owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
