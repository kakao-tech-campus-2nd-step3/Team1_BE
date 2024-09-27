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

@Entity(name = "project")
public class ProjectEntity extends BaseEntity{

    public ProjectEntity() {

    }

    public ProjectEntity(String name, Integer isDelete, User user,
        LocalDateTime startDate,
        LocalDateTime endDate) {
        this.name = name;
        this.isDelete = isDelete;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "project_id")
    private Long id;

    @Column(name = "name")
    private String name;

//    @Column(name = "view_type")
//    private Object viewType;

    @Column(name = "is_delete")
    private Integer isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL)
    private List<GuestEntity> guestEntities;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL)
    private List<ProjectOption> options;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    //Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public Integer getIsDelete() {
        return isDelete;
    }

    public User getUser() {
        return user;
    }

    public List<GuestEntity> getGuests() {
        return guestEntities;
    }

    public List<ProjectOption> getOptions() {
        return options;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    //Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setGuests(List<GuestEntity> guestEntities) {
        this.guestEntities = guestEntities;
    }

    public void setOptions(List<ProjectOption> options) {
        this.options = options;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void addGuest(GuestEntity guestEntity) {
        if (this.guestEntities == null) {
            this.guestEntities = new ArrayList<>();
        }
        this.guestEntities.add(guestEntity);
        guestEntity.setProject(this);  // 양방향 관계 설정
    }

    public void addOption(ProjectOption option) {
        if (this.options == null) {
            this.options = new ArrayList<>();
        }
        this.options.add(option);
        option.setProject(this);  // 양방향 관계 설정
    }

}
