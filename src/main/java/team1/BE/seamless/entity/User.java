package team1.BE.seamless.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
public class User extends BaseEntity{

    public User() {

    }

    public User(String email, String password, Integer isDelete) {
        this.email = email;
        this.password = password;
        this.isDelete = isDelete;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "is_delete")
    private Integer isDelete;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ProjectEntity> projectEntities;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public List<ProjectEntity> getProjects() {
        return projectEntities;
    }

    public void addProject(ProjectEntity projectEntity) {
        if (this.projectEntities == null) {
            this.projectEntities = new ArrayList<>();
        }
        this.projectEntities.add(projectEntity);
        projectEntity.setUser(this);  // 양방향 관계 설정
    }

}
