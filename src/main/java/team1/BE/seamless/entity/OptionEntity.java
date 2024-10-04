package team1.BE.seamless.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity(name = "option")
public class OptionEntity extends BaseEntity {

    public OptionEntity() {

    }

    public OptionEntity(String name, String eventType) {
        this.name = name;
        this.eventType = eventType;
        this.isDeleted = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "option_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @OneToMany(mappedBy = "optionEntity", cascade = CascadeType.ALL)
    private List<ProjectOption> options;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEventType() {
        return eventType;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public List<ProjectOption> getOptions() {
        return options;
    }

    public void setOptions(List<ProjectOption> options) {
        this.options = options;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
