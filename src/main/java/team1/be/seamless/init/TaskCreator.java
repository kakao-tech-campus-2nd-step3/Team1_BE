package team1.be.seamless.init;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team1.be.seamless.dto.TaskDTO.TaskCreate;
import team1.be.seamless.entity.enums.Priority;
import team1.be.seamless.entity.enums.TaskStatus;
import team1.be.seamless.service.TaskService;

@Component
public class TaskCreator {

    private final TaskService taskService;

    @Autowired
    public TaskCreator(TaskService taskService) {
        this.taskService = taskService;
    }

    public void creator() {

        TaskCreate task1 = new TaskCreate("태스크1", "첫번째 태스크입니다.", 1L,
            LocalDateTime.of(2024, 10, 10, 0, 0),
            LocalDateTime.of(2025, 9, 3, 0, 0), Priority.HIGH, TaskStatus.IN_PROGRESS, 1);

        taskService.createTask(1L, task1);
    }
}
