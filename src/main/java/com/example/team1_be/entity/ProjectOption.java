package com.example.team1_be.entity;

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

    public ProjectOption(String name, Integer isDelete, Project project) {
        this.name = name;
        this.isDelete = isDelete;
        this.project = project;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "option_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_delete")
    private Integer isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL)
    private List<ProjectOptionDetail> details;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public Project getProject() {
        return project;
    }

    public List<ProjectOptionDetail> getDetails() {
        return details;
    }

    public void addDetail(ProjectOptionDetail detail) {
        if (this.details == null) {
            this.details = new ArrayList<>();
        }
        this.details.add(detail);
        detail.setOption(this);  // 양방향 관계 설정
    }

    public void setProject(Project project) {
        this.project = project;
    }

}
