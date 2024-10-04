package team1.BE.seamless.mapper;

import org.springframework.stereotype.Component;
import team1.BE.seamless.DTO.TaskDTO.Create;
import team1.BE.seamless.DTO.TaskDTO.Update;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.TaskEntity;
import team1.BE.seamless.util.Util;

@Component
public class TaskMapper {

    public TaskEntity toEntity(ProjectEntity project, MemberEntity member, Create create) {
        return new TaskEntity(
            create.getName(),
            create.getRemark(),
            project,
            member,
            create.getStartDate(),
            create.getEndDate());
    }

    public TaskEntity toUpdate(TaskEntity task, Update update) {
        task.setName(Util.isNull(update.getName()) ? task.getName() : update.getName());
        task.setRemark(Util.isNull(update.getRemark()) ? task.getRemark() : update.getRemark());
        task.setProgress(Util.isNull(update.getProgress().toString()) ? task.getProgress()
            : update.getProgress());
        task.setStartDate(Util.isNull(update.getStartDate().toString()) ? task.getStartDate()
            : update.getStartDate());
        task.setEndDate(
            Util.isNull(update.getEndDate().toString()) ? task.getEndDate() : update.getEndDate());

        return task;
    }
}
