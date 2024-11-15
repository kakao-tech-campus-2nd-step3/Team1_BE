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

        TaskCreate task2 = new TaskCreate("태스크2", "두번째 태스크입니다.", null,
                LocalDateTime.of(2024, 10, 10, 0, 0),
                LocalDateTime.of(2025, 9, 3, 0, 0), Priority.LOW, TaskStatus.IN_PROGRESS, 1);
        taskService.createTask(1L, task2);

        TaskCreate task3 = new TaskCreate("태스크3", "세번째 태스크입니다.", 2L,
                LocalDateTime.of(2024, 10, 10, 0, 0),
                LocalDateTime.of(2025, 9, 3, 0, 0), Priority.MEDIUM, TaskStatus.PENDING, 1);
        taskService.createTask(1L, task3);

        TaskCreate task4 = new TaskCreate("태스크4", "네번째 태스크입니다.", 3L,
                LocalDateTime.of(2024, 10, 10, 0, 0),
                LocalDateTime.of(2025, 9, 3, 0, 0), Priority.LOW, TaskStatus.PENDING, 1);
        taskService.createTask(1L, task4);

        TaskCreate task5 = new TaskCreate("태스크5", "다섯번째 태스크입니다.", 4L,
                LocalDateTime.of(2024, 10, 10, 0, 0),
                LocalDateTime.of(2025, 9, 3, 0, 0), Priority.HIGH, TaskStatus.IN_PROGRESS, 1);
        taskService.createTask(1L, task5);

        TaskCreate task6 = new TaskCreate("태스크6", "여섯번째 태스크입니다.", 5L,
                LocalDateTime.of(2024, 10, 10, 0, 0),
                LocalDateTime.of(2025, 9, 3, 0, 0), Priority.MEDIUM, TaskStatus.IN_PROGRESS, 1);
        taskService.createTask(1L, task6);

        TaskCreate task7 = new TaskCreate("태스크7", "일곱번째 태스크입니다.", null,
                LocalDateTime.of(2024, 10, 10, 0, 0),
                LocalDateTime.of(2025, 9, 3, 0, 0), Priority.MEDIUM, TaskStatus.COMPLETED, 1);
        taskService.createTask(1L, task7);

        TaskCreate task8 = new TaskCreate("태스크8", "여덟번째 태스크입니다.", 1L,
                LocalDateTime.of(2024, 10, 10, 0, 0),
                LocalDateTime.of(2025, 9, 3, 0, 0), Priority.LOW, TaskStatus.IN_PROGRESS, 1);
        taskService.createTask(1L, task8);

        TaskCreate task9 = new TaskCreate("태스크9", "아홉번째 태스크입니다.", null,
                LocalDateTime.of(2024, 10, 10, 0, 0),
                LocalDateTime.of(2025, 9, 3, 0, 0), Priority.MEDIUM, TaskStatus.PENDING, 1);
        taskService.createTask(1L, task9);

        TaskCreate task10 = new TaskCreate("태스크10", "열번째 태스크입니다.", 4L,
                LocalDateTime.of(2024, 10, 10, 0, 0),
                LocalDateTime.of(2025, 9, 3, 0, 0), Priority.MEDIUM, TaskStatus.COMPLETED, 1);
        taskService.createTask(1L, task10);
    }
}
