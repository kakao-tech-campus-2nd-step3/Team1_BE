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

@Entity(name = "guest")
public class Guest extends BaseEntity{

    public Guest() {

    }

    public Guest(String email, String joinNumber, Integer isDelete, Project project) {
        this.email = email;
        this.joinNumber = joinNumber;
        this.isDelete = isDelete;
        this.project = project;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "guest_id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "join_number")
    private String joinNumber;

    @Column(name = "is_delete")
    private Integer isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Task> tasks;

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

    public Project getProject() {
        return project;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        if (this.tasks == null) {
            this.tasks = new ArrayList<>();
        }
        this.tasks.add(task);
        task.setOwner(this);  // 양방향 관계 설정
    }

    public void setProject(Project project) {
        this.project = project;
    }

}
