package team1.BE.seamless.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team1.BE.seamless.DTO.TaskDTO.Create;
import team1.BE.seamless.DTO.TaskDTO.Update;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.TaskEntity;
import team1.BE.seamless.entity.enums.Role;
import team1.BE.seamless.mapper.TaskMapper;
import team1.BE.seamless.repository.MemberRepository;
import team1.BE.seamless.repository.ProjectRepository;
import team1.BE.seamless.repository.TaskRepository;
import team1.BE.seamless.util.auth.ParsingPram;
import team1.BE.seamless.util.errorException.BaseHandler;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final TaskMapper taskMapper;
    private final ParsingPram parsingPram;

    @Autowired
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository,
        MemberRepository memberRepository, TaskMapper taskMapper, ParsingPram parsingPram) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.taskMapper = taskMapper;
        this.parsingPram = parsingPram;
    }

    public TaskEntity getTask(Long taskId) {
        return taskRepository.findById(taskId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 태스크"));
    }

    public List<TaskEntity> getTaskList(Long projectId) {
        ProjectEntity projectEntity = projectRepository.findById(projectId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트"));

        return taskRepository.findByProjectEntity(projectEntity);
    }

    public TaskEntity createTask(HttpServletRequest req, @Valid Long projectId, Create create) {
        ProjectEntity project = projectRepository.findByIdAndUserEmail(projectId,parsingPram.getEmail(req))
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트"));

//        태스크의 일정 검증
        if (project.getStartDate().isBefore(create.getStartDate()) || project.getStartDate().isAfter(create.getEndDate())){
            throw new BaseHandler(HttpStatus.FORBIDDEN,"태스크는 프로젝트의 기한을 넘어설 수 없습니다.");
        }

        MemberEntity member = memberRepository.findById(create.getMemberId())
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 멤버"));

        TaskEntity taskEntity = taskMapper.toEntity(project,member,create);

        taskRepository.save(taskEntity);
        return taskEntity;
    }

    @Transactional
    public TaskEntity updateTask(HttpServletRequest req, @Valid Long taskId, @Valid Update update) {
        TaskEntity task = taskRepository.findById(taskId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 태스크"));

//        태스크의 일정 검증
        if (task.getProject().getStartDate().isBefore(update.getStartDate()) || task.getProject().getStartDate().isAfter(update.getEndDate())){
            throw new BaseHandler(HttpStatus.FORBIDDEN,"태스크는 프로젝트의 기한을 넘어설 수 없습니다.");
        }

//        수정 권한이 있는지 검증
//        팀장
        if (parsingPram.getRole(req).equals(Role.USER.toString())){
            if (!task.getProject().getUser().getEmail().equals(parsingPram.getEmail(req))){
                throw new BaseHandler(HttpStatus.UNAUTHORIZED,"태스크 수정 권한이 없습니다.");
            }
//            멤버 변경
            MemberEntity member = memberRepository.findById(update.getMemberId())
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 멤버"));

            task.setOwner(member);
        }
//        팀원
        if (parsingPram.getRole(req).equals(Role.USER.toString())){
            if (!task.getOwner().getEmail().equals(parsingPram.getEmail(req))){
                throw new BaseHandler(HttpStatus.UNAUTHORIZED,"태스크 수정 권한이 없습니다.");
            }
        }

        taskMapper.toUpdate(task,update);
        return task;
    }

    @Transactional
    public Long deleteTask(HttpServletRequest req, Long taskId) {
        TaskEntity task = taskRepository.findByIdAndProjectEntityUserEmail(taskId, parsingPram.getEmail(req))
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 태스크"));

        task.setDeleted(true);

        return task.getId();
    }
}