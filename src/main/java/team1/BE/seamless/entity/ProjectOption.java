package team1.BE.seamless.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "project_option")
public class ProjectOption extends BaseEntity {

    public ProjectOption() {

    }

    public ProjectOption(OptionEntity optionEntity) {
        this.isDeleted = false;
        this.optionEntity = optionEntity;
    }

    public ProjectOption(ProjectEntity projectEntity, OptionEntity optionEntity) {
        this.isDeleted = false;
        this.projectEntity = projectEntity;
        this.optionEntity = optionEntity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "project_option_id")
    private Long id;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private OptionEntity optionEntity;

    public Long getId() {
        return id;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public ProjectEntity getProjectEntity() {
        return projectEntity;
    }

    public OptionEntity getOptionEntity() {
        return optionEntity;
    }

    public void setProjectEntity(ProjectEntity projectEntity) {
        this.projectEntity = projectEntity;
    }

    public void setOptionEntity(OptionEntity optionEntity) {
        this.optionEntity = optionEntity;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
