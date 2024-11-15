package team1.be.seamless.mapper;


import java.util.List;

import org.springframework.stereotype.Component;
import team1.be.seamless.dto.ProjectDTO;
import team1.be.seamless.dto.ProjectDTO.ProjectDate;
import team1.be.seamless.dto.ProjectDTO.ProjectDetail;
import team1.be.seamless.dto.ProjectDTO.ProjectManager;
import team1.be.seamless.dto.ProjectDTO.ProjectUpdate;
import team1.be.seamless.entity.OptionEntity;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.entity.ProjectOptionEntity;
import team1.be.seamless.entity.UserEntity;
import team1.be.seamless.util.Util;

@Component
public class ProjectMapper {

    public ProjectEntity toEntity(ProjectDTO.ProjectCreate create, UserEntity userEntity,
                                  List<ProjectOptionEntity> projectOptionEntities) {
        return new ProjectEntity(
                create.getName(),
                create.getDescription(),
                create.getImageURL(),
                userEntity,
                projectOptionEntities,
                create.getStartDate(),
                create.getEndDate()
        );
    }

    public ProjectEntity toUpdate(ProjectEntity entity, ProjectUpdate update,
                                  List<ProjectOptionEntity> projectOptionEntities) {
        return entity.update(
                Util.isNull(update.getName()) ? entity.getName() : update.getName(),
                Util.isNull(update.getDescription()) ? entity.getDescription()
                        : update.getDescription(),
                Util.isNull(update.getImageURL()) ? entity.getImageURL() : update.getImageURL(),
                Util.isNull(update.getStartDate().toString()) ? entity.getStartDate()
                        : update.getStartDate(),
                Util.isNull(update.getEndDate().toString()) ? entity.getEndDate() : update.getEndDate(),
                projectOptionEntities
        );
    }

    public ProjectDetail toDetail(ProjectEntity projectEntity) {
        return new ProjectDTO.ProjectDetail(
                projectEntity.getId(),
                projectEntity.getName(),
                projectEntity.getDescription(),
                projectEntity.getImageURL(),
                projectEntity.getStartDate(),
                projectEntity.getEndDate(),
                projectEntity.getProjectOptions().stream()
                        .map(ProjectOptionEntity::getOptionEntity).map(OptionEntity::getId).toList(),
                projectEntity.getMemberEntities().stream()
                        .filter(entity -> !entity.getIsDelete()).toList().size(),
                toManager(projectEntity.getUserEntity())
        );
    }

    public ProjectDate toDate(ProjectEntity projectEntity) {
        return new ProjectDate(
                projectEntity.getId(),
                projectEntity.getName(),
                projectEntity.getStartDate(),
                projectEntity.getEndDate()
        );
    }

    public ProjectManager toManager(UserEntity userEntity) {
        return new ProjectManager(
                userEntity.getName(),
                userEntity.getPicture()
        );
    }

}
