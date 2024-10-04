package team1.BE.seamless.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team1.BE.seamless.DTO.TaskDTO;
import team1.BE.seamless.entity.TaskEntity;
import team1.BE.seamless.service.TaskService;
import team1.BE.seamless.util.page.PageMapper;
import team1.BE.seamless.util.page.PageResult;
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
    public PageResult<TaskEntity> getTaskList(@PathVariable Long projectId,
        @Valid TaskDTO.getList param) {
        return PageMapper.toPageResult(taskService.getTaskList(projectId, param));
    }

    /**
     * 팀장만 태스크를 생성할 수 있음
     */
    @Operation(summary = "태스크 생성")
    @PostMapping("/{projectId}/task")
    public SingleResult<TaskEntity> createTask(HttpServletRequest req,
        @Valid @PathVariable Long projectId, @Valid @RequestBody TaskDTO.Create create) {
        return new SingleResult<>(taskService.createTask(req, projectId, create));
    }

    /**
     * 멤버도 코드로 프로젝트 참여에 성공하면 토큰을 반환함 태스크 수정은 팀장, 태스크를 수행하고 있는 팀원이 가능함 태스크의 멤버를 수정하는 건 팀장만 가능
     */
    @Operation(summary = "태스크 수정")
    @PutMapping("/task/{taskId}")
    public SingleResult<TaskEntity> updateTask(HttpServletRequest req,
        @Valid @PathVariable Long taskId,
        @Valid @RequestBody TaskDTO.Update update) {
        return new SingleResult<>(taskService.updateTask(req, taskId, update));
    }

    /**
     * 팀장만 태스크를 삭제할 수 있음
     */
    @Operation(summary = "태스크 삭제")
    @DeleteMapping("/task/{taskId}")
    public SingleResult<Long> deleteTask(HttpServletRequest req, @PathVariable Long taskId) {
        return new SingleResult<>(taskService.deleteTask(req, taskId));
    }
}