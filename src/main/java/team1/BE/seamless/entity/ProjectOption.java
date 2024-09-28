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

@Entity(name = "project_option")
public class ProjectOption extends BaseEntity{

    public ProjectOption() {

    }

    public ProjectOption(String name,  ProjectEntity projectEntity) {
        this.name = name;
        this.isDeleted = false;
        this.projectEntity = projectEntity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "option_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL)
    private List<ProjectOptionDetail> details;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public ProjectEntity getProject() {
        return projectEntity;
    }

    public List<ProjectOptionDetail> getDetails() {
        return details;
    }

    public void setProject(ProjectEntity projectEntity) {
        this.projectEntity = projectEntity;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
