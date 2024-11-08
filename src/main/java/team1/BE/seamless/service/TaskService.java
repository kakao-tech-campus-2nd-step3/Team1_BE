package team1.BE.seamless.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team1.BE.seamless.DTO.TaskDTO.MemberProgress;
import team1.BE.seamless.DTO.TaskDTO.ProjectProgress;
import team1.BE.seamless.DTO.TaskDTO.TaskCreate;
import team1.BE.seamless.DTO.TaskDTO.TaskDetail;
import team1.BE.seamless.DTO.TaskDTO.TaskUpdate;
import team1.BE.seamless.DTO.TaskDTO.TaskWithOwnerDetail;
import team1.BE.seamless.DTO.TaskDTO.getList;
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

    public TaskDetail getTask(Long taskId) {
        TaskEntity taskEntity = taskRepository.findByIdAndIsDeletedFalse(taskId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 태스크"));
        Long projectId = taskEntity.getProject().getId();

        ProjectEntity project = projectRepository.findByIdAndIsDeletedFalse(projectId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트"));
        return taskMapper.toDetail(taskEntity);
    }

    public Page<TaskWithOwnerDetail> getTaskList(Long projectId, Integer status, String priority,
        String ownerName, getList param) {
//        ProjectEntity project = projectRepository.findByIdAndIsDeletedFalse(projectId)
//            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트"));

        //멤버 아이디에 대한 쿼리 파라미터가 존재할때 : null일때
        Long memberId = null;

        if (ownerName != null) {
            MemberEntity memberEntity = memberRepository.findByName(ownerName)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 멤버"));
            memberId = memberEntity.getId();
        }

        Page<TaskEntity> taskEntities = taskRepository.findByProjectIdAndOptionalFilters(projectId,
            status, priority, memberId, param.toPageable());

        return taskEntities.map(taskMapper::toDetailWithOwner);
    }

    public ProjectProgress getProjectProgress(Long projectId, getList param) {
//        ProjectEntity project = projectRepository.findByIdAndIsDeletedFalse(projectId)
//            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트"));

        Page<TaskEntity> taskEntities = taskRepository.findAllByProjectEntityIdAndIsDeletedFalse(
            projectId, param.toPageable());

        int sum = taskEntities.getContent().stream().mapToInt(TaskEntity::getProgress).sum();
        int count = taskEntities.getContent().size();

        if (count == 0) {
            throw new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트에 할당된 태스크가 존재하지 않습니다.");
        }

        int average = sum / count;
        String growthLevel;
        String description;

        switch (average / 25) {
            case 0: // 0~25
                growthLevel = "새싹";
                description = "아직 새싹이네요. 본격적으로 프로젝트를 시작해볼까요?";
                break;
            case 1: // 26~50
                growthLevel = "묘목";
                description = "나무가 자라기 시작했어요. 팀원들과 함께 나무를 열심히 키워봐요!";
                break;
            case 2: // 51~75
                growthLevel = "어린 나무";
                description = "나무가 자라고 있어요. 프로젝트에 더욱더 박차를 가해봐요!";
                break;
            case 3: // 76~100
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

//        List<MemberEntity> teamMembers = project.getMemberEntities().stream().toList();
//
//        List<MemberProgress> teamMemberProgress = teamMembers.stream().map(member -> {
//            List<TaskEntity> activeTasks = taskRepository.findByOwnerIdAndProjectEntityAndIsDeletedFalse(member.getId(), project);
//
//            int averageProgress = activeTasks.isEmpty() ? 0 : (int) activeTasks.stream().mapToInt(TaskEntity::getProgress).average().orElse(0);
//
//            return new MemberProgress(member, averageProgress, activeTasks);
//        }).toList();

//        return new PageImpl<>(teamMemberProgress, param.toPageable(), teamMembers.size());

//        결과를 담을 리스트
        List<MemberProgress> teamMemberProgress = new ArrayList<>();

//        삭제되지 않음 멤버들
        List<MemberEntity> memberEntities = project.getMemberEntities().stream()
            .filter(member -> Boolean.FALSE.equals(member.getIsDelete())).toList();

        if (param.getPage() > 0 && param.getPage() * param.getSize() >= memberEntities.size()) {
            return new PageImpl<>(teamMemberProgress, param.toPageable(), 0);
        }

//        페이지에 해당하는 멤버들만 순회해서 연산의 수를 감소
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
//                   태스크가 삭제되지 않았고, 해당 테스크의 담당이라면
                if (Boolean.FALSE.equals(task.getIsDeleted()) && task.getOwner().equals(member)) {
                    tasks.add(task);
                }
            }

//               해당 멤버의 프로그레스의 평균을 사용
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

    public TaskDetail createTask(HttpServletRequest req, Long projectId, TaskCreate taskCreate) {
        ProjectEntity project = projectRepository.findByIdAndUserEntityEmailAndIsDeletedFalse(
                projectId, parsingPram.getEmail(req))
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트"));

//        태스크의 일정 검증
        if (project.getStartDate().isAfter(taskCreate.getStartDate()) || project.getEndDate()
            .isBefore(taskCreate.getEndDate())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "태스크는 프로젝트의 기한을 넘어설 수 없습니다.");
        }

        List<MemberEntity> teamMembers = project.getMemberEntities().stream().toList();
        boolean isTeamMember = teamMembers.stream()
            .anyMatch(member -> member.getId().equals(taskCreate.getOwnerId()));
        if (!isTeamMember) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "해당 프로젝트와 관련된 팀원이 아닙니다.");
        }

        MemberEntity member = memberRepository.findById(taskCreate.getOwnerId())
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 멤버"));

        TaskEntity taskEntity = taskMapper.toEntity(project, member, taskCreate);

        taskRepository.save(taskEntity);

        return taskMapper.toDetail(taskEntity);
    }

    // 테스트용 오버로딩
    public TaskDetail createTask(Long projectId, TaskCreate taskCreate) {

        ProjectEntity project = projectRepository.findByIdAndUserEntityEmailAndIsDeletedFalse(
                projectId, "user1@google.com")
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트"));

//        태스크의 일정 검증
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
    public TaskDetail updateTask(HttpServletRequest req, Long taskId, TaskUpdate update) {
        TaskEntity task = taskRepository.findByIdAndIsDeletedFalse(taskId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 태스크"));

//        태스크의 일정 검증
        if (task.getProject().getStartDate().isAfter(update.getStartDate()) || task.getProject()
            .getEndDate().isBefore(update.getEndDate())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "태스크는 프로젝트의 기한을 넘어설 수 없습니다.");
        }

//        수정 권한이 있는지 검증
//        팀장
        if (parsingPram.getRole(req).equals(Role.USER.toString())) {
            if (!task.getProject().getUserEntity().getEmail().equals(parsingPram.getEmail(req))) {
                throw new BaseHandler(HttpStatus.UNAUTHORIZED, "태스크 수정 권한이 없습니다.");
            }
//            멤버 변경
            MemberEntity member = memberRepository.findById(update.getOwnerId())
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 멤버"));

            task.setOwner(member);
        }
//        팀원
        if (parsingPram.getRole(req).equals(Role.MEMBER.toString())) {
            if (!task.getOwner().getEmail().equals(parsingPram.getEmail(req))) {
                throw new BaseHandler(HttpStatus.UNAUTHORIZED, "태스크 수정 권한이 없습니다.");
            }
        }

        taskMapper.toUpdate(task, update);

        return taskMapper.toDetail(task);
    }

    @Transactional
    public Long deleteTask(HttpServletRequest req, Long taskId) {
        TaskEntity task = taskRepository.findByIdAndProjectEntityUserEntityEmail(taskId,
                parsingPram.getEmail(req))
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 태스크"));

        task.setDeleted(true);

        return task.getId();
    }
}