package team1.BE.seamless.mapper;


import org.springframework.stereotype.Component;
import team1.BE.seamless.DTO.ProjectDTO;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.UserEntity;

@Component
public class ProjectMapper {

    public ProjectEntity toEntity(ProjectDTO.ProjectCreate create, UserEntity userEntity) {
        return new ProjectEntity(
            create.getName(),
            userEntity,
            create.getStartDate(),
            create.getEndDate()
        );
    }

}
