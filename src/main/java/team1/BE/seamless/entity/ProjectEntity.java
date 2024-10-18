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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "projectss")
public class ProjectEntity extends BaseEntity {

    public ProjectEntity() {
//        memberEntities = new ArrayList<>();
//        projectOptions = new ArrayList<>();
//        taskEntities = new ArrayList<>();
    }

    public ProjectEntity(String name, UserEntity userEntity, List<ProjectOption> projectOptions,
        LocalDateTime startDate,
        LocalDateTime endDate) {
        this.name = name;
        this.isDeleted = false;
        this.userEntity = userEntity;
        this.projectOptions = projectOptions;
        this.startDate = startDate;
        this.endDate = endDate;
//        memberEntities = new ArrayList<>();
//        taskEntities = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "project_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL)
    private List<MemberEntity> memberEntities = new ArrayList<>();

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL)
    private List<ProjectOption> projectOptions = new ArrayList<>();

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL)
    private List<TaskEntity> taskEntities = new ArrayList<>();

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


    public boolean getIsDeleted() {
        return isDeleted;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public List<MemberEntity> getMemberEntities() {
        return memberEntities;
    }

    public List<ProjectOption> getProjectOptions() {
        return projectOptions;
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

    public void setProjectOptions(List<ProjectOption> projectOptions) {
        this.projectOptions = projectOptions;
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