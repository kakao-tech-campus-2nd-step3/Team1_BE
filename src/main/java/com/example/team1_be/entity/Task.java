package com.example.team1_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity(name = "task")
public class Task {

    public Task() {

    }

    public Task(String name, String remark, Integer progress, Integer isDelete, Project project,
        Guest owner, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.remark = remark;
        this.progress = progress;
        this.isDelete = isDelete;
        this.project = project;
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
    private Integer progress;

    @Column(name = "is_delete")
    private Integer isDelete;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private Guest owner;

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

    public Integer getIsDelete() {
        return isDelete;
    }

    public Project getProject() {
        return project;
    }

    public Guest getOwner() {
        return owner;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setOwner(Guest owner) {
        this.owner = owner;
    }

    public void setProject(Project project) {
        this.project = project;
    }

}
