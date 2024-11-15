package team1.be.seamless.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team1.be.seamless.dto.TaskDTO;
import team1.be.seamless.dto.TaskDTO.MemberProgress;
import team1.be.seamless.dto.TaskDTO.ProjectProgress;
import team1.be.seamless.dto.TaskDTO.TaskCreate;
import team1.be.seamless.dto.TaskDTO.TaskDetail;
import team1.be.seamless.dto.TaskDTO.TaskUpdate;
import team1.be.seamless.dto.TaskDTO.TaskWithOwnerDetail;
import team1.be.seamless.service.TaskService;
import team1.be.seamless.util.auth.ParsingParam;
import team1.be.seamless.util.page.PageMapper;
import team1.be.seamless.util.page.PageResult;
import team1.be.seamless.util.page.SingleResult;

@Tag(name = "태스크")
@RestController
@RequestMapping("/api/project")
public class TaskController {

    private final TaskService taskService;
    private final ParsingParam parsingParam;

    @Autowired
    public TaskController(TaskService taskService, ParsingParam parsingParam) {
        this.taskService = taskService;
        this.parsingParam = parsingParam;
    }

    @Operation(summary = "태스크 단건 조회")
    @GetMapping("/task/{taskId}")
    public SingleResult<TaskDetail> getTask(@PathVariable("taskId") Long taskId) {
        return new SingleResult<>(taskService.getTask(taskId));
    }

    @Operation(summary = "프로젝트 아이디로 태스크 리스트 조회")
    @GetMapping("/{projectId}/task")
    public PageResult<TaskWithOwnerDetail> getTaskList(@PathVariable("projectId") Long projectId,
                                                       @RequestParam(value = "status", required = false) String status,
                                                       @RequestParam(value = "priority", required = false) String priority,
                                                       @RequestParam(value = "owner", required = false) Long ownerId,
                                                       @Valid TaskDTO.getList param) {

        return PageMapper.toPageResult(
                taskService.getTaskList(projectId, status, priority, ownerId, param));
    }

    @Operation(summary = "팀 전체 진행도 확인")
    @GetMapping("/{projectId}/progress")
    public SingleResult<ProjectProgress> getProjectProgress(
            @PathVariable("projectId") Long projectId,
            @Valid TaskDTO.getList param) {
        return new SingleResult<>(taskService.getProjectProgress(projectId, param));
    }

    @Operation(summary = "팀원 개별 진행도 및 할당된 태스크 확인")
    @GetMapping("/{projectId}/task/progress")
    public PageResult<MemberProgress> getMemberProgress(@PathVariable("projectId") Long projectId,
                                                        @Valid TaskDTO.getList param) {
        return PageMapper.toPageResult(taskService.getMemberProgress(projectId, param));
    }

    /**
     * 팀장만 태스크를 생성할 수 있음
     */
    @Operation(summary = "태스크 생성")
    @PostMapping("/{projectId}/task")
    public SingleResult<TaskDetail> createTask(HttpServletRequest req,
                                               @Valid @PathVariable("projectId") Long projectId,
                                               @Valid @RequestBody TaskCreate taskCreate) {
        return new SingleResult<>(taskService.createTask(parsingParam.getEmail(req), parsingParam.getRole(req), projectId, taskCreate));
    }

    /**
     * 멤버도 코드로 프로젝트 참여에 성공하면 토큰을 반환함 태스크 수정은 팀장, 태스크를 수행하고 있는 팀원이 가능함 태스크의 멤버를 수정하는 건 팀장만 가능
     */
    @Operation(summary = "태스크 수정")
    @PutMapping("/task/{taskId}")
    public SingleResult<TaskDetail> updateTask(HttpServletRequest req,
                                               @Valid @PathVariable("taskId") Long taskId,
                                               @Valid @RequestBody TaskUpdate update) {
        return new SingleResult<>(taskService.updateTask(parsingParam.getRole(req), parsingParam.getEmail(req), taskId, update));
    }

    /**
     * 팀장만 태스크를 삭제할 수 있음
     */
    @Operation(summary = "태스크 삭제")
    @DeleteMapping("/task/{taskId}")
    public SingleResult<Long> deleteTask(HttpServletRequest req,
                                         @PathVariable("taskId") Long taskId) {
        return new SingleResult<>(taskService.deleteTask(parsingParam.getRole(req), parsingParam.getEmail(req), taskId));
    }
}