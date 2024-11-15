package team1.be.seamless.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import team1.be.seamless.dto.TaskDTO.MemberProgress;
import team1.be.seamless.dto.TaskDTO.ProjectProgress;
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
import team1.be.seamless.repository.ProjectRepository;
import team1.be.seamless.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)

public class ProgressServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

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
    void 프로젝트_진행도_조회_성공() {
        Page<TaskEntity> tasks = new PageImpl<>(List.of(taskEntity1, taskEntity2));
        taskParam = new getList();

        when(taskRepository.findAllByProjectEntityIdAndIsDeletedFalse(any(Long.class), eq(taskParam.toPageable()))).thenReturn(tasks);

        ProjectProgress result = taskService.getProjectProgress(1L, taskParam);

        assertThat(result).isNotNull();
        verify(taskRepository, times(1)).findAllByProjectEntityIdAndIsDeletedFalse(
                1L, taskParam.toPageable());

    }

    @Test
    void 팀원별_진행도_및_태스크_리스트_조회() {
        taskParam = new getList();
        taskParam.setPage(0);
        taskParam.setSize(10);
        projectEntity.setMemberEntities(List.of(memberEntity));
        projectEntity.setTaskEntity(List.of(taskEntity1, taskEntity2));

        when(projectRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(projectEntity));

        Page<MemberProgress> result = taskService.getMemberProgress(1L, taskParam);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getProgress()).isEqualTo(50);
        verify(projectRepository).findByIdAndIsDeletedFalse(1L);
    }
}