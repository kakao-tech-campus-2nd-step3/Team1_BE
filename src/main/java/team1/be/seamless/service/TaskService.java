package team1.be.seamless.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team1.be.seamless.dto.TaskDTO.*;
import team1.be.seamless.entity.MemberEntity;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.entity.TaskEntity;
import team1.be.seamless.entity.enums.Role;
import team1.be.seamless.mapper.TaskMapper;
import team1.be.seamless.repository.MemberRepository;
import team1.be.seamless.repository.ProjectRepository;
import team1.be.seamless.repository.TaskRepository;
import team1.be.seamless.util.errorException.BaseHandler;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository,
                       MemberRepository memberRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.taskMapper = taskMapper;
    }

    public TaskDetail getTask(Long taskId) {
        TaskEntity taskEntity = taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 태스크"));

        if (!taskEntity.getProject().isActive()) {
            throw new BaseHandler(HttpStatus.NOT_FOUND, "태스크가 속한 프로젝트가 존재 하지 않습니다.");
        }

        return taskMapper.toDetail(taskEntity);
    }

    public Page<TaskWithOwnerDetail> getTaskList(Long projectId, String status, String priority, Long ownerId, getList param) {

        Page<TaskEntity> taskEntities = taskRepository.findByProjectIdAndOptionalFilters(
                projectId, status, priority, ownerId, param.toPageable());

        return new PageImpl<>(
                taskEntities.stream()
                        .filter(taskEntity -> taskEntity.getProject().isActive())
                        .map(taskMapper::toDetailWithOwner)
                        .toList(),
                taskEntities.getPageable(),
                taskEntities.getTotalElements()
        );
    }

    public ProjectProgress getProjectProgress(Long projectId, getList param) {

        Page<TaskEntity> taskEntities = taskRepository.findAllByProjectEntityIdAndIsDeletedFalse(
                projectId, param.toPageable());

        int sum = taskEntities.getContent().stream().mapToInt(TaskEntity::getProgress).sum();
        int count = taskEntities.getContent().size();

        int average = (count == 0) ? 0 : sum / count;
        String growthLevel;
        String description;

        switch (average / 25) {
            case 0:
                growthLevel = "새싹";
                description = "아직 새싹이네요. 본격적으로 프로젝트를 시작해볼까요?";
                break;
            case 1:
                growthLevel = "묘목";
                description = "나무가 자라기 시작했어요. 팀원들과 함께 나무를 열심히 키워봐요!";
                break;
            case 2:
                growthLevel = "어린 나무";
                description = "나무가 자라고 있어요. 프로젝트에 더욱더 박차를 가해봐요!";
                break;
            case 3:
                growthLevel = "성장한 나무";
                description = "나무가 거의 다 자랐어요. 프로젝트 마무리까지 화이팅!";
                break;
            default:
                growthLevel = "나무 성장단계";
                description = "나무를 키워요";
        }
        return new ProjectProgress(projectId, average, growthLevel, description);
    }

    public Page<MemberProgress> getMemberProgress(Long projectId, getList param) {
        ProjectEntity project = projectRepository.findByIdAndIsDeletedFalse(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트"));

        List<MemberProgress> teamMemberProgress = new ArrayList<>();

        List<MemberEntity> memberEntities = project.getMemberEntities().stream()
                .filter(member -> Boolean.FALSE.equals(member.getIsDelete())).toList();

        if (param.getPage() > 0 && param.getPage() * param.getSize() >= memberEntities.size()) {
            return new PageImpl<>(teamMemberProgress, param.toPageable(), 0);
        }

        int start;

        if (param.getPage() * param.getSize() >= memberEntities.size()) {
            start = 0;
        } else {
            start = param.getPage() * param.getSize();
        }

        for (MemberEntity member : memberEntities.subList(start,
                Math.min((start + param.getSize()), project.getMemberEntities().size()))) {
            List<TaskEntity> tasks = new ArrayList<>();
            for (TaskEntity task : project.getTaskEntities()) {

                if (Boolean.FALSE.equals(task.getIsDeleted()) && task.getOwner().equals(member)) {
                    tasks.add(task);
                }
            }

            teamMemberProgress.add(new MemberProgress(
                    member
                    , (int) Math.round(tasks.stream()
                    .mapToInt(task -> task.getProgress())
                    .average()
                    .orElse(0.0))
                    , tasks));
        }

        return new PageImpl<>(teamMemberProgress, param.toPageable(), memberEntities.size());
    }

    public TaskDetail createTask(String email, String role, Long projectId, TaskCreate taskCreate) {
        ProjectEntity project = projectRepository.findByIdAndIsDeletedFalse(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트"));

        if (Role.MEMBER.isRole(role)) {
            if (!project.getMemberEntities().stream()
                    .anyMatch(memberEntity -> memberEntity.getEmail().equals(email) && Boolean.FALSE.equals(memberEntity.getIsDelete()))) {
                throw new BaseHandler(HttpStatus.UNAUTHORIZED, "해당 프로젝트에 소속되어 있지 않습니다.");
            }


        } else if (Role.USER.isRole(role)) {
            if (!project.getUserEntity().getEmail().equals(email)) {
                throw new BaseHandler(HttpStatus.UNAUTHORIZED, "해당 프로젝트에 소속되어 있지 않습니다.");
            }
        }

        if (project.getStartDate().isAfter(taskCreate.getStartDate()) || project.getEndDate()
                .isBefore(taskCreate.getEndDate())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "태스크는 프로젝트의 기한을 넘어설 수 없습니다.");
        }

        MemberEntity member = null;
        if (taskCreate.getOwnerId() != null) {
            member = memberRepository.findByIdAndIsDeleteFalse(taskCreate.getOwnerId())
                    .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 멤버"));
        }

        TaskEntity taskEntity = taskMapper.toEntity(project, member, taskCreate);

        taskRepository.save(taskEntity);

        return taskMapper.toDetail(taskEntity);
    }

    @Profile("test")
    public TaskDetail createTask(Long projectId, TaskCreate taskCreate) {

        ProjectEntity project = projectRepository.findByIdAndIsDeletedFalse(
                        projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트"));

        if (project.getStartDate().isAfter(taskCreate.getStartDate()) || project.getEndDate()
                .isBefore(taskCreate.getEndDate())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "태스크는 프로젝트의 기한을 넘어설 수 없습니다.");
        }

        MemberEntity member = memberRepository.findById(taskCreate.getOwnerId())
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 멤버"));

        TaskEntity taskEntity = taskMapper.toEntity(project, member, taskCreate);

        taskRepository.save(taskEntity);

        return taskMapper.toDetail(taskEntity);
    }

    @Transactional
    public TaskDetail updateTask(String role, String email, Long taskId, TaskUpdate update) {
        TaskEntity task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 태스크"));

        if (task.getProject().getStartDate().isAfter(update.getStartDate()) || task.getProject()
                .getEndDate().isBefore(update.getEndDate())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "태스크는 프로젝트의 기한을 넘어설 수 없습니다.");
        }

        if (Role.USER.isRole(role)) {
            if (!task.getProject().getUserEntity().getEmail().equals(email)) {
                throw new BaseHandler(HttpStatus.UNAUTHORIZED, "태스크 수정 권한이 없습니다.");
            }
            if (update.getOwnerId() == null) {
                task.setOwner(null);
            } else {
                MemberEntity member = memberRepository.findByIdAndIsDeleteFalse(update.getOwnerId())
                        .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 멤버"));

                task.setOwner(member);
            }
        }

        if (Role.MEMBER.isRole(role)) {
            if (!task.getOwner().getEmail().equals(email)) {
                throw new BaseHandler(HttpStatus.UNAUTHORIZED, "태스크 수정 권한이 없습니다.");
            }
        }

        taskMapper.toUpdate(task, update);

        return taskMapper.toDetail(task);
    }

    @Transactional
    public Long deleteTask(String role, String email, Long taskId) {

        if (!Role.USER.isRole(role)) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED, "태스크 삭제 권한이 없습니다.");
        }
        TaskEntity task = taskRepository.findByIdAndProjectEntityUserEntityEmail(taskId, email)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 태스크"));

        task.setDeleted(true);

        return task.getId();
    }
}