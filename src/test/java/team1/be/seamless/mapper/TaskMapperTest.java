package team1.be.seamless.mapper;

import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import team1.be.seamless.dto.TaskDTO.TaskCreate;
import team1.be.seamless.dto.TaskDTO.TaskDetail;
import team1.be.seamless.dto.TaskDTO.TaskUpdate;
import team1.be.seamless.dto.TaskDTO.TaskWithOwnerDetail;
import team1.be.seamless.entity.MemberEntity;
import team1.be.seamless.entity.OptionEntity;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.entity.ProjectOptionEntity;
import team1.be.seamless.entity.TaskEntity;
import team1.be.seamless.entity.UserEntity;
import team1.be.seamless.entity.enums.Priority;
import team1.be.seamless.entity.enums.TaskStatus;

public class TaskMapperTest {

    private TaskMapper taskMapper;

    private TaskEntity taskEntity;

    private ProjectEntity projectEntity;

    private OptionEntity optionEntity1;

    private OptionEntity optionEntity2;

    private List<ProjectOptionEntity> projectOptions;

    private UserEntity userEntity;

    private MemberEntity memberEntity;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper();
        userEntity = new UserEntity(
            "사용자1", "user1@google.com", "user1Image.jpg"
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
            LocalDateTime.of(2023, 11, 21, 0, 0, 0),
            LocalDateTime.of(2025, 11, 21, 0, 0, 0)
        );

        memberEntity = new MemberEntity("멤버 1", "MEMBER", "member1@gmail.com",
            "https://abcdgef.com", projectEntity);

        taskEntity = new TaskEntity(
            "태스크 1",
            "첫번째 태스크 입니다.",
            Priority.HIGH,
            projectEntity,
            memberEntity,
            LocalDateTime.of(2023, 12, 22, 0, 0, 0),
            LocalDateTime.of(2024, 12, 22, 0, 0, 0),
            75,
            TaskStatus.PENDING
        );
    }

    @Test
    void 생성_시_taskCreate_에서_Entity로_변환_검증() {
        TaskCreate create = new TaskCreate(
            "새로운 태스크",
            "새로운 태스크 입니다.",
            memberEntity.getId(),
            LocalDateTime.of(2024, 10, 10, 0, 0),
            LocalDateTime.of(2024, 11, 11, 0, 0),
            Priority.HIGH,
            TaskStatus.IN_PROGRESS,
            50);

        TaskEntity created_taskEntity = new TaskMapper().toEntity(projectEntity, memberEntity,
            create);

        Assertions.assertThat(created_taskEntity.getName()).isEqualTo("새로운 태스크");
        Assertions.assertThat(created_taskEntity.getDescription()).isEqualTo("새로운 태스크 입니다.");
        Assertions.assertThat(created_taskEntity.getStartDate())
            .isEqualTo(LocalDateTime.of(2024, 10, 10, 0, 0));
        Assertions.assertThat(created_taskEntity.getEndDate())
            .isEqualTo(LocalDateTime.of(2024, 11, 11, 0, 0));
        Assertions.assertThat(created_taskEntity.getPriority()).isEqualTo(Priority.HIGH);
        Assertions.assertThat(created_taskEntity.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        Assertions.assertThat(created_taskEntity.getProgress()).isEqualTo(50);
    }

    @Test
    void 수정_시_taskUpdate_에서_Entity로_변환_검증() {
        TaskUpdate update = new TaskUpdate(
            "수정된 태스크",
            "",
            1,
            memberEntity.getId(),
            LocalDateTime.of(2024, 10, 10, 0, 0),
            LocalDateTime.of(2024, 11, 20, 0, 0),
            Priority.LOW,
            TaskStatus.IN_PROGRESS
        );

        TaskEntity updated_taskEntity = new TaskMapper().toUpdate(taskEntity, update);

        Assertions.assertThat(updated_taskEntity.getName()).isEqualTo("수정된 태스크");
        Assertions.assertThat(updated_taskEntity.getDescription()).isEqualTo("");
        Assertions.assertThat(updated_taskEntity.getStartDate())
            .isEqualTo(LocalDateTime.of(2024, 10, 10, 0, 0));
        Assertions.assertThat(updated_taskEntity.getEndDate())
            .isEqualTo(LocalDateTime.of(2024, 11, 20, 0, 0));
        Assertions.assertThat(updated_taskEntity.getPriority()).isEqualTo(Priority.LOW);
        Assertions.assertThat(updated_taskEntity.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        Assertions.assertThat(updated_taskEntity.getProgress()).isEqualTo(1);
    }

    @Test
    void TaskEntity가_TaskDetail로_반환_되는지_검증() {
        TaskDetail taskDetail = taskMapper.toDetail(taskEntity);

        Assertions.assertThat(taskDetail.getName()).isEqualTo("태스크 1");
        Assertions.assertThat(taskDetail.getDescription()).isEqualTo("첫번째 태스크 입니다.");
        Assertions.assertThat(taskDetail.getStartDate())
            .isEqualTo(LocalDateTime.of(2023, 12, 22, 0, 0));
        Assertions.assertThat(taskDetail.getEndDate())
            .isEqualTo(LocalDateTime.of(2024, 12, 22, 0, 0));
        Assertions.assertThat(taskDetail.getPriority()).isEqualTo(Priority.HIGH);
        Assertions.assertThat(taskDetail.getStatus()).isEqualTo(TaskStatus.PENDING);
        Assertions.assertThat(taskDetail.getProgress()).isEqualTo(75);
    }

    @Test
    void TaskEntity가_TaskWithOwnerDetail로_반환_되는지_검증() {
        TaskWithOwnerDetail taskWithOwnerDetail = taskMapper.toDetailWithOwner(taskEntity);

        Assertions.assertThat(taskWithOwnerDetail.getName()).isEqualTo("태스크 1");
        Assertions.assertThat(taskWithOwnerDetail.getDescription()).isEqualTo("첫번째 태스크 입니다.");
        Assertions.assertThat(taskWithOwnerDetail.getOwner().getName()).isEqualTo("멤버 1");
        Assertions.assertThat(taskWithOwnerDetail.getOwner().getRole()).isEqualTo("MEMBER");
        Assertions.assertThat(taskWithOwnerDetail.getOwner().getImageURL())
            .isEqualTo("https://abcdgef.com");
        Assertions.assertThat(taskWithOwnerDetail.getStartDate())
            .isEqualTo(LocalDateTime.of(2023, 12, 22, 0, 0));
        Assertions.assertThat(taskWithOwnerDetail.getEndDate())
            .isEqualTo(LocalDateTime.of(2024, 12, 22, 0, 0));
        Assertions.assertThat(taskWithOwnerDetail.getPriority()).isEqualTo(Priority.HIGH);
        Assertions.assertThat(taskWithOwnerDetail.getStatus()).isEqualTo(TaskStatus.PENDING);
        Assertions.assertThat(taskWithOwnerDetail.getProgress()).isEqualTo(75);
    }
}