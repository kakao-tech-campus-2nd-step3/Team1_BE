package team1.BE.seamless.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import team1.BE.seamless.DTO.TaskDTO;
import team1.BE.seamless.entity.TaskEntity;
import team1.BE.seamless.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team1.BE.seamless.util.page.ListResult;
import team1.BE.seamless.util.page.SingleResult;

@Tag(name = "태스크")
@RestController
@RequestMapping("/api/project")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "태스크 단건 조회")
    @GetMapping("/task/{taskId}")
    public SingleResult<TaskEntity> getTask(@PathVariable Long taskId) {
        return new SingleResult<>(taskService.getTask(taskId));
    }

    @Operation(summary = "프로젝트 아이디로 태스크 리스트 조회 ")
    @GetMapping("/{projectId}/task")
    public ListResult<TaskEntity> getTaskList(@PathVariable Long projectId) {
        return new ListResult<>(taskService.getTaskList(projectId));
    }

    @Operation(summary = "태스크 생성")
    @PostMapping("/{projectId}/task")
    public SingleResult<TaskEntity> createTask(@Valid @RequestBody TaskDTO req) {
        return new SingleResult<>(taskService.createTask(req));
    }

    @Operation(summary = "태스크 수정")
    @PutMapping("/task/{taskId}")
    public SingleResult<TaskEntity> updateTask(@PathVariable Long taskId, @Valid @RequestBody TaskDTO req) {
        return new SingleResult<>(taskService.updateTask(taskId, req));
    }

    @Operation(summary = "태스크 삭제")
    @DeleteMapping("/task/{taskId}")
    public SingleResult<Long> deleteTask(@PathVariable Long taskId) {
        return new SingleResult<>(taskService.deleteTask(taskId));
    }
}