package com.example.team1_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "project_option_detail")
public class ProjectOptionDetail extends BaseEntity{

    public ProjectOptionDetail() {

    }

    public ProjectOptionDetail(String name, String eventType, Integer isDelete,
        ProjectOption option) {
        this.name = name;
        this.eventType = eventType;
        this.isDelete = isDelete;
        this.option = option;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "detail_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "is_delete")
    private Integer isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private ProjectOption option;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEventType() {
        return eventType;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public ProjectOption getOption() {
        return option;
    }

    public void setOption(ProjectOption option) {
        this.option = option;
    }

}
