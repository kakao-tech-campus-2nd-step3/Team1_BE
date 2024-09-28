package team1.BE.seamless.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "project")
public class ProjectEntity extends BaseEntity{

    public ProjectEntity() {

    }

    public ProjectEntity(String name, UserEntity userEntity,
        LocalDateTime startDate,
        LocalDateTime endDate) {
        this.name = name;
        this.isDeleted = false;
        this.userEntity = userEntity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "project_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL)
    private List<MemberEntity> memberEntities;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL)
    private List<ProjectOption> options;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL)
    private List<TaskEntity> taskEntities;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    //Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public boolean getIsDeleted() {
        return isDeleted;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public List<MemberEntity> getMemberEntities() {
        return memberEntities;
    }

    public List<ProjectOption> getOptions() {
        return options;
    }

    public List<TaskEntity> getTaskEntities() {
        return taskEntities;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    //Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setUser(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public void setMemberEntities(List<MemberEntity> memberEntities) {
        this.memberEntities = memberEntities;
    }

    public void setOptions(List<ProjectOption> options) {
        this.options = options;
    }

    public void setTaskEntity(List<TaskEntity> taskEntities) {
        this.taskEntities = taskEntities;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public ProjectEntity update(String name, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

}