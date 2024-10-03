package team1.BE.seamless.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import team1.BE.seamless.DTO.TaskDTO;
import team1.BE.seamless.entity.TaskEntity;
import team1.BE.seamless.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team1.BE.seamless.util.page.SingleResult;

@RestController
@RequestMapping("/api/project")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{projectId}/task")
    public List<TaskEntity> getTaskList(@PathVariable Long projectId) {
        List<TaskEntity> taskList = taskService.getTaskList(projectId);
        return taskList;
    }

    @PostMapping("/{projectId}/task")
    public SingleResult<TaskEntity> createTask(@Valid @RequestBody TaskDTO req) {
        return new SingleResult<>(taskService.createTask(req));
    }

    @DeleteMapping("/task/{taskId}")
    public SingleResult<Long> deleteTask(@PathVariable Long taskId) {
        return new SingleResult<>(taskService.deleteTask(taskId));
    }
}