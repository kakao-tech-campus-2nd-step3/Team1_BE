package team1.BE.seamless.mapper;

import org.springframework.stereotype.Component;
import team1.BE.seamless.DTO.TaskDTO.TaskCreate;
import team1.BE.seamless.DTO.TaskDTO.TaskDetail;
import team1.BE.seamless.DTO.TaskDTO.TaskUpdate;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.TaskEntity;
import team1.BE.seamless.util.Util;

@Component
public class TaskMapper {

    public TaskEntity toEntity(ProjectEntity project, MemberEntity member, TaskCreate taskCreate) {
        return new TaskEntity(
            taskCreate.getName(),
            taskCreate.getRemark(),
            project,
            member,
            taskCreate.getStartDate(),
            taskCreate.getEndDate());
    }

    public TaskEntity toUpdate(TaskEntity task, TaskUpdate update) {
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


    public TaskDetail toDetail(TaskEntity task) {
        return new TaskDetail(task.getId(), task.getName(), task.getRemark(), task.getOwner().getId(), task.getProgress(), task.getStartDate(), task.getEndDate());
    }
}
