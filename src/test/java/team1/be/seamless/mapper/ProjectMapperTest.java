package team1.be.seamless.mapper;

import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import team1.be.seamless.dto.ProjectDTO;
import team1.be.seamless.dto.ProjectDTO.ProjectCreate;
import team1.be.seamless.dto.ProjectDTO.ProjectDate;
import team1.be.seamless.dto.ProjectDTO.ProjectDetail;
import team1.be.seamless.dto.ProjectDTO.ProjectManager;
import team1.be.seamless.dto.ProjectDTO.ProjectUpdate;
import team1.be.seamless.entity.OptionEntity;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.entity.ProjectOptionEntity;
import team1.be.seamless.entity.UserEntity;

public class ProjectMapperTest {

    private ProjectMapper projectMapper;

    private OptionEntity optionEntity1;

    private OptionEntity optionEntity2;

    private List<ProjectOptionEntity> projectOptions;

    private UserEntity userEntity;

    private ProjectEntity projectEntity1;

    @BeforeEach
    void setUp() {
        projectMapper = new ProjectMapper();
        userEntity = new UserEntity(
            "사용자1", "user1@google.com", "user1Image.jpg"
        );

        optionEntity1 = new OptionEntity("옵션1", "옵션 설명1", "타입1");
        optionEntity2 = new OptionEntity("옵션2", "옵션 설명2", "타입2");

        ProjectOptionEntity projectOption1 = new ProjectOptionEntity(optionEntity1);
        ProjectOptionEntity projectOption2 = new ProjectOptionEntity(optionEntity2);
        projectOptions = List.of(projectOption1, projectOption2);

        projectEntity1 = new ProjectEntity(
            "프로젝트1",
            "프로젝트 설명1",
            "https://example.com/project1.jpg",
            userEntity,
            projectOptions,
            LocalDateTime.of(2024,11,21,0,0,0),
            LocalDateTime.of(2025,11,21,0,0,0)
        );
    }

    @Test
    void 생성_시_ProjectCreate_에서_Entity_로_변환_검증() {
        ProjectDTO.ProjectCreate create = new ProjectCreate(
            "프로젝트1",
            "프로젝트 설명1",
            "https://example.com/project1.jpg",
            List.of(1L, 2L),
            LocalDateTime.of(2024, 10, 1, 0, 0, 0),
            LocalDateTime.of(2025, 10, 1, 0, 0, 0)
        );

        ProjectEntity projectEntity = projectMapper.toEntity(create, userEntity, projectOptions);

        assertThat(projectEntity.getName()).isEqualTo("프로젝트1");
        assertThat(projectEntity.getDescription()).isEqualTo("프로젝트 설명1");
        assertThat(projectEntity.getImageURL()).isEqualTo("https://example.com/project1.jpg");
        assertThat(projectEntity.getStartDate()).isEqualTo(LocalDateTime.of(2024, 10, 1, 0, 0, 0));
        assertThat(projectEntity.getEndDate()).isEqualTo(LocalDateTime.of(2025, 10, 1, 0, 0, 0));
        assertThat(projectEntity.getUserEntity()).isEqualTo(userEntity);
        assertThat(projectEntity.isActive()).isTrue();
        assertThat(projectEntity.isExpired()).isFalse();
    }

    @Test
    void 수정시_해당_Entity가_업데이트_되는지_검증() {
        ProjectDTO.ProjectUpdate update = new ProjectUpdate(
            "프로젝트2",
            "프로젝트 설명2",
            "https://example.com/project2.jpg",
            List.of(1L, 2L),
            LocalDateTime.of(2024, 10, 1, 0, 0, 0),
            LocalDateTime.of(2026, 10, 1, 0, 0, 0)
        );

        ProjectEntity projectEntity = projectMapper.toUpdate(projectEntity1, update, projectOptions);

        assertThat(projectEntity.getName()).isEqualTo("프로젝트2");
        assertThat(projectEntity.getDescription()).isEqualTo("프로젝트 설명2");
        assertThat(projectEntity.getImageURL()).isEqualTo("https://example.com/project2.jpg");
        assertThat(projectEntity.getStartDate()).isEqualTo(LocalDateTime.of(2024, 10, 1, 0, 0, 0));
        assertThat(projectEntity.getEndDate()).isEqualTo(LocalDateTime.of(2026, 10, 1, 0, 0, 0));
    }

    @Test
    void ProjectEntity가_ProjectDetail로_반환_되는_지_검증() {
        ProjectDetail projectDetail = projectMapper.toDetail(projectEntity1);

        assertThat(projectDetail.getName()).isEqualTo("프로젝트1");
        assertThat(projectDetail.getDescription()).isEqualTo("프로젝트 설명1");
        assertThat(projectDetail.getImageURL()).isEqualTo("https://example.com/project1.jpg");
        assertThat(projectDetail.getTotalMembers()).isEqualTo(0);
        assertThat(projectDetail.getProjectManager().getName()).isEqualTo(userEntity.getName());
        assertThat(projectDetail.getProjectManager().getImageURL()).isEqualTo(userEntity.getPicture());
        assertThat(projectDetail.getStartDate()).isEqualTo(LocalDateTime.of(2024,11,21,0,0,0));
        assertThat(projectDetail.getEndDate()).isEqualTo(LocalDateTime.of(2025,11,21,0,0,0));
    }

    @Test
    void ProjectEntity가_ProjectDate로_반환_되는_지_검증() {

        ProjectDate projectDate = projectMapper.toDate(projectEntity1);


        assertThat(projectDate.getName()).isEqualTo("프로젝트1");
        assertThat(projectDate.getStartDate()).isEqualTo(LocalDateTime.of(2024,11,21,0,0,0));
        assertThat(projectDate.getEndDate()).isEqualTo(LocalDateTime.of(2025,11,21,0,0,0));
    }

    @Test
    void UserEntity가_ProjectManager로_반환_되는_지_검증() {

        ProjectManager projectManager = projectMapper.toManager(userEntity);


        assertThat(projectManager.getName()).isEqualTo("사용자1");
        assertThat(projectManager.getImageURL()).isEqualTo("user1Image.jpg");
    }

}
