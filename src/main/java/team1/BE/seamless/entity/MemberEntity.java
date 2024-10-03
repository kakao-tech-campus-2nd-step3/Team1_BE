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
import java.util.ArrayList;
import java.util.List;

@Entity(name = "member")
public class MemberEntity extends BaseEntity{

    public MemberEntity() {

    }

    public MemberEntity(String email, String joinNumber, Integer isDelete, ProjectEntity projectEntity) {
        this.email = email;
        this.joinNumber = joinNumber;
        this.isDelete = isDelete;
        this.projectEntity = projectEntity;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setJoinNumber(String joinNumber) {
        this.joinNumber = joinNumber;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "join_number")
    private String joinNumber;

    @Column(name = "is_delete")
    private Integer isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<TaskEntity> taskEntities;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getJoinNumber() {
        return joinNumber;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public ProjectEntity getProject() {
        return projectEntity;
    }

    public List<TaskEntity> getTasks() {
        return taskEntities;
    }

    public void addTask(TaskEntity taskEntity) {
        if (this.taskEntities == null) {
            this.taskEntities = new ArrayList<>();
        }
        this.taskEntities.add(taskEntity);
        taskEntity.setOwner(this);  // 양방향 관계 설정
    }

    public void setProject(ProjectEntity projectEntity) {
        this.projectEntity = projectEntity;
    }

}
