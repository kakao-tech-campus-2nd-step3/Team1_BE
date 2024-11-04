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
            taskCreate.getDescription(),
            taskCreate.getPriority(),
            project,
            member,
            taskCreate.getStartDate(),
            taskCreate.getEndDate(),
            taskCreate.getProgress(),
            taskCreate.getStatus());
    }

    public TaskEntity toUpdate(TaskEntity task, TaskUpdate update) {
        task.setName(Util.isNull(update.getName()) ? task.getName() : update.getName());
        task.setDescription(Util.isNull(update.getDescription()) ? task.getDescription() : update.getDescription());
        task.setProgress(update.getProgress() == null ? task.getProgress() : update.getProgress());
        task.setStartDate(update.getStartDate() == null ? task.getStartDate() : update.getStartDate());
        task.setEndDate(update.getEndDate() == null ? task.getEndDate() : update.getEndDate());
        task.setStatus(update.getStatus() == null ? task.getStatus() : update.getStatus());
        task.setPriority(update.getPriority() == null ? task.getPriority() : update.getPriority());

        return task;
    }

    public TaskDetail toDetail(TaskEntity task) {
        return new TaskDetail(task.getId(), task.getName(), task.getDescription(),
            task.getOwner().getId(), task.getProgress(), task.getStartDate(), task.getEndDate(),
            task.getPriority(), task.getStatus());
    }
}
