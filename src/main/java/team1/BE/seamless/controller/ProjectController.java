package team1.BE.seamless.controller;

import team1.BE.seamless.DTO.ProjectDTO;
import team1.BE.seamless.entity.GuestEntity;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.service.ProjectService;
import team1.BE.seamless.util.page.ListResult;
import team1.BE.seamless.util.page.PageMapper;
import team1.BE.seamless.util.page.PageResult;
import team1.BE.seamless.util.page.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "프로젝트")
@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "프로젝트 리스트 조회")
    @GetMapping
    public PageResult<ProjectEntity> getProjectList(@Valid ProjectDTO.getList param) {
        return PageMapper.toPageResult(projectService.getProjectList(param));
    }

    @Operation(summary = "프로젝트 조회")
    @GetMapping("/{id}")
    public SingleResult<ProjectEntity> getProject(@Valid @PathVariable long id) {
        return new SingleResult<>(projectService.getProject(id));
    }

    @Operation(summary = "프로젝트 멤버 조회")
    @GetMapping("/{id}/{members}")
    public ListResult<GuestEntity> getProjectMembers(@Valid @PathVariable long id) {
        return new ListResult<>(projectService.getProjectMembers(id));
    }

    @Operation(summary = "프로젝트 생성")
    @PostMapping
    public SingleResult<ProjectEntity> createProject(@Valid @RequestBody ProjectDTO.create create) {
        return new SingleResult<>(projectService.createProject(create));
    }

    @Operation(summary = "프로젝트 설정 수정")
    @PutMapping("/{id}")
    public SingleResult<ProjectEntity> updateProject(@Valid @RequestBody ProjectDTO.update update,
        @PathVariable long id) {
        return new SingleResult<>(projectService.updateProject(id, update));
    }

    @Operation(summary = "프로젝트 삭제")
    @DeleteMapping("/{id}")
    public void deleteProject(@Valid @PathVariable long id) {
        projectService.deleteProject(id);
    }

}
