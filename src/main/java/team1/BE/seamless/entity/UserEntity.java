package team1.BE.seamless.entity;

import team1.BE.seamless.entity.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING) // Enum 타입은 문자열 형태로 저장해야 함
    @NotNull
    private Role role;

    @NotNull
    @Column
    private Integer isDelete;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public UserEntity() {

    }

    public UserEntity(String name, String email, String picture) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.isDelete = 0;
        this.role = Role.USER;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPicture() {
        return picture;
    }

    public @NotNull Role getRole() {
        return role;
    }

    public @NotNull Integer getIsDelete() {
        return isDelete;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setIsDelete() {
        this.isDelete = 1;
    }

    public UserEntity update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }


}
