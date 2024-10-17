package team1.BE.seamless.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import team1.BE.seamless.entity.enums.OptionType;

@Entity(name = "option")
public class OptionEntity extends BaseEntity {

    public OptionEntity() {

    }

    public OptionEntity(String name, String description, OptionType optionType) {
        this.name = name;
        this.description = description;
        this.optionType = optionType;
        this.isDeleted = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "option_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "option_type")
    @Enumerated(EnumType.STRING)
    private OptionType optionType;

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

    public String getDescription() {
        return description;
    }

    public OptionType getOptionType() {
        return optionType;
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
