package team1.BE.seamless.mapper;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.UserEntity;

@Component
public class ProjectMapper {

    public ProjectEntity toEntity(String name, UserEntity user, LocalDateTime startDate, LocalDateTime endDate) {
        return new ProjectEntity(name, user, startDate, endDate);
    }

}
