package team1.be.seamless.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import team1.be.seamless.dto.TaskDTO;
import team1.be.seamless.dto.TaskDTO.TaskCreate;
import team1.be.seamless.dto.TaskDTO.TaskDetail;
import team1.be.seamless.dto.TaskDTO.TaskUpdate;
import team1.be.seamless.dto.TaskDTO.TaskWithOwnerDetail;
import team1.be.seamless.dto.TaskDTO.getList;
import team1.be.seamless.entity.MemberEntity;
import team1.be.seamless.entity.OptionEntity;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.entity.ProjectOptionEntity;
import team1.be.seamless.entity.TaskEntity;
import team1.be.seamless.entity.UserEntity;
import team1.be.seamless.entity.enums.Priority;
import team1.be.seamless.entity.enums.Role;
import team1.be.seamless.entity.enums.TaskStatus;
import team1.be.seamless.mapper.TaskMapper;
import team1.be.seamless.repository.MemberRepository;
import team1.be.seamless.repository.ProjectRepository;
import team1.be.seamless.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskMapper taskMapper;

    private String email;

    private String role;

    private TaskDTO.getList taskParam;

    private ProjectEntity projectEntity;

    private UserEntity userEntity;

    private OptionEntity optionEntity1;

    private OptionEntity optionEntity2;

    private MemberEntity memberEntity;

    private TaskEntity taskEntity1;

    private TaskEntity taskEntity2;

    private List<ProjectOptionEntity> projectOptions;

    @BeforeEach
    void setUp() {
        email = "user@example.com";
        role = Role.USER.getKey();

        userEntity = new UserEntity(
            "사용자1",
            email,
            "https://example.com/user1.jpg"
        );

        optionEntity1 = new OptionEntity("옵션1", "옵션 설명1", "타입1");
        optionEntity2 = new OptionEntity("옵션2", "옵션 설명2", "타입2");

        ProjectOptionEntity projectOption1 = new ProjectOptionEntity(optionEntity1);
        ProjectOptionEntity projectOption2 = new ProjectOptionEntity(optionEntity2);
        projectOptions = List.of(projectOption1, projectOption2);

        projectEntity = new ProjectEntity(
            "프로젝트1",
            "프로젝트 설명1",
            "https://example.com/project1.jpg",
            userEntity,
            projectOptions,
            LocalDateTime.of(2024, 11, 21, 0, 0, 0),
            LocalDateTime.of(2025, 11, 21, 0, 0, 0)
        );

        memberEntity = new MemberEntity("멤버 1", "MEMBER", "member1@gmail.com", "member1.jpeg",
            projectEntity);

        taskEntity1 = new TaskEntity("태스크 1", "첫번째 태스크 입니다.", Priority.HIGH, projectEntity,
            memberEntity, LocalDateTime.of(2024, 10, 10, 0, 0),
            LocalDateTime.of(2024, 11, 11, 0, 0), 50, TaskStatus.IN_PROGRESS);

        taskEntity2 = new TaskEntity("태스크 2", "두번째 태스크 입니다.", Priority.HIGH, projectEntity,
            memberEntity, LocalDateTime.of(2024, 10, 10, 0, 0),
            LocalDateTime.of(2024, 11, 11, 0, 0), 50, TaskStatus.IN_PROGRESS);
    }

    @Test
    void 태스크_리스트_페이지네이션_반환_검증() {
        // Given
        Page<TaskEntity> tasks = new PageImpl<>(List.of(taskEntity1, taskEntity2));
        taskParam = new getList();

        when(taskRepository.findByProjectIdAndOptionalFilters(
            1L,
            "IN_PROGRESS",
            "HIGH",
            memberEntity.getId(),
            taskParam.toPageable())).thenReturn(tasks);
        when(memberRepository.findByName("멤버 1")).thenReturn(Optional.of(memberEntity));
        when(taskMapper.toDetailWithOwner(any(TaskEntity.class)))
            .thenReturn(mock(TaskDTO.TaskWithOwnerDetail.class));

        // When
        Page<TaskWithOwnerDetail> result = taskService.getTaskList(
            1L,
            "IN_PROGRESS",
            "HIGH",
            "멤버 1",
            taskParam);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(taskRepository, times(1)).findByProjectIdAndOptionalFilters(
            1L, "IN_PROGRESS", "HIGH", memberEntity.getId(), taskParam.toPageable());
        verify(memberRepository, times(1)).findByName("멤버 1"); // 멤버 조회가 1번 호출되었는지 확인
        verify(taskMapper, atLeastOnce()).toDetailWithOwner(any(TaskEntity.class));
    }

    @Test
    void 태스크_생성_성공() {
        // Given
        TaskCreate create = new TaskCreate("새로운 태스크",
            "새로운 태스크 입니다.", 1L,
            LocalDateTime.of(2024, 12, 10, 0, 0), LocalDateTime.of(2024, 12, 11, 0, 0),
            Priority.HIGH,
            TaskStatus.IN_PROGRESS,
            50);

        TaskEntity created_task = new TaskEntity("새로운 태스크",
            "새로운 태스크 입니다.",
            Priority.HIGH,
            projectEntity,
            memberEntity,
            LocalDateTime.of(2024, 12, 10, 0, 0),
            LocalDateTime.of(2024, 12, 11, 0, 0),
            50,
            TaskStatus.IN_PROGRESS);

        when(projectRepository.findByIdAndIsDeletedFalse(any(Long.class))).thenReturn(
            Optional.of(projectEntity));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(memberEntity));
        when(taskMapper.toEntity(projectEntity, memberEntity, create)).thenReturn(created_task);
        when(taskMapper.toDetail(created_task))
            .thenReturn(mock(TaskDTO.TaskDetail.class));

        // When
        TaskDetail result = taskService.createTask(1L, create);

        // Then
        assertThat(result).isNotNull();
        verify(projectRepository).findByIdAndIsDeletedFalse(1L);
        verify(taskRepository).save(any(TaskEntity.class));
        verify(taskMapper).toDetail(any(TaskEntity.class));
    }

    @Test
    void 태스크_수정_성공() {
        // Given
        TaskUpdate update = new TaskUpdate(
            "수정된 태스크",
            "수정된 태스크입니다.",
            78,
            1L,
            LocalDateTime.of(2024, 12, 10, 0, 0),
            LocalDateTime.of(2024, 12, 11, 0, 0),
            Priority.LOW,
            TaskStatus.IN_PROGRESS
        );

        TaskEntity updated_task = new TaskEntity("수정된 태스크",
            "수정된 태스크 입니다.",
            Priority.LOW,
            projectEntity,
            memberEntity,
            LocalDateTime.of(2024, 12, 10, 0, 0),
            LocalDateTime.of(2024, 12, 11, 0, 0),
            78,
            TaskStatus.IN_PROGRESS);

        when(taskRepository.findByIdAndIsDeletedFalse(any(Long.class))).thenReturn(
            Optional.of(taskEntity1));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(memberEntity));
        when(taskMapper.toUpdate(taskEntity1, update)).thenReturn(updated_task);
        when(taskMapper.toDetail(any(TaskEntity.class)))
            .thenReturn(mock(TaskDTO.TaskDetail.class));

        // When
        TaskDetail result = taskService.updateTask(role, email, 1L, update);

        // Then
        assertThat(result).isNotNull();
        verify(taskRepository, times(1)).findByIdAndIsDeletedFalse(1L);
        verify(memberRepository, times(1)).findById(1L);
        verify(taskMapper, times(1)).toUpdate(taskEntity1, update);
        verify(taskMapper, times(1)).toDetail(any(TaskEntity.class));
    }

    @Test
    void 태스크_삭제_검증() {
        // Given
        long taskId = 1L;
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setIsDeleted(false);

        when(taskRepository.findByIdAndProjectEntityUserEntityEmail(taskId, email)).thenReturn(
            Optional.of(taskEntity));

        // When
        Long result = taskService.deleteTask(role, email, taskId);

        // Then
        assertThat(result).isEqualTo(taskId);
        assertThat(taskEntity.getIsDeleted()).isTrue();
        verify(taskRepository, times(1)).findByIdAndProjectEntityUserEntityEmail(taskId, email);
    }
}