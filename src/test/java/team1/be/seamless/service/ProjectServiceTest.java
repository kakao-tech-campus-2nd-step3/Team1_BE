package team1.be.seamless.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import team1.be.seamless.dto.OptionDTO.OptionDetail;
import team1.be.seamless.dto.ProjectDTO;
import team1.be.seamless.dto.ProjectDTO.ProjectDate;
import team1.be.seamless.dto.ProjectDTO.ProjectDetail;
import team1.be.seamless.entity.OptionEntity;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.entity.ProjectOptionEntity;
import team1.be.seamless.entity.UserEntity;
import team1.be.seamless.entity.enums.Role;
import team1.be.seamless.mapper.OptionMapper;
import team1.be.seamless.mapper.ProjectMapper;
import team1.be.seamless.repository.OptionRepository;
import team1.be.seamless.repository.ProjectOptionRepository;
import team1.be.seamless.repository.ProjectRepository;
import team1.be.seamless.repository.UserRepository;
import team1.be.seamless.util.errorException.BaseHandler;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ProjectOptionRepository projectOptionRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private OptionMapper optionMapper;

    private String email;

    private String role;

    private ProjectDTO.getList param;

    private ProjectEntity projectEntity1;

    private ProjectEntity projectEntity2;

    private UserEntity userEntity;

    private OptionEntity optionEntity1;

    private OptionEntity optionEntity2;

    private List<ProjectOptionEntity> projectOptions;


    @BeforeEach
    void setUp() {
        email = "user@example.com";
        role = Role.USER.getKey();
        param = new ProjectDTO.getList();

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

        projectEntity1 = new ProjectEntity(
                "프로젝트1",
                "프로젝트 설명1",
                "https://example.com/project1.jpg",
                userEntity,
                projectOptions,
                LocalDateTime.of(2024, 11, 21, 0, 0, 0),
                LocalDateTime.of(2025, 11, 21, 0, 0, 0)
        );

        projectEntity2 = new ProjectEntity(
                "프로젝트2",
                "프로젝트 설명2",
                "https://example.com/project2.jpg",
                userEntity,
                projectOptions,
                LocalDateTime.of(2024, 11, 21, 0, 0, 0),
                LocalDateTime.of(2025, 11, 21, 0, 0, 0)
        );
    }

    @Test
    void 프로젝트_리스트_페이지네이션_반환_검증() {
        Page<ProjectEntity> projects = new PageImpl<>(List.of(projectEntity1, projectEntity2));
        given(projectRepository.findAllByUserEntityEmailAndIsDeletedFalse(param.toPageable(), email)).willReturn(projects);

        Page<ProjectDetail> result = projectService.getProjectList(param, email, role);

        assertThat(result).isNotNull();
        then(projectRepository).should().findAllByUserEntityEmailAndIsDeletedFalse(param.toPageable(), email);
    }

    @Test
    void 존재_하지_않는_프로젝트_조회_시_예외() {

        long projectId = 1L;
        given(projectRepository.findByIdAndIsDeletedFalse(projectId)).willReturn(Optional.empty());


        assertThatThrownBy(() -> projectService.getProject(projectId, email, role))
                .isInstanceOf(BaseHandler.class);
        then(projectRepository).should().findByIdAndIsDeletedFalse(projectId);
    }

    @Test
    void 프로젝트에_적용된_옵션들_조회() {

        long projectId = 1L;
        given(projectRepository.findByIdAndIsDeletedFalse(projectId)).willReturn(Optional.of(projectEntity1));

        OptionDetail optionDetail1 = new OptionDetail();
        OptionDetail optionDetail2 = new OptionDetail();

        given(optionMapper.toDetail(optionEntity1)).willReturn(optionDetail1);
        given(optionMapper.toDetail(optionEntity2)).willReturn(optionDetail2);

        List<OptionDetail> result = projectService.getProjectOptions(projectId, email, role);

        assertThat(result).isNotNull();
        then(projectRepository).should().findByIdAndIsDeletedFalse(projectId);
        then(optionMapper).should().toDetail(optionEntity1);
        then(optionMapper).should().toDetail(optionEntity2);
    }

    @Test
    void 프로젝트_기간_조회() {
        Page<ProjectEntity> projects = new PageImpl<>(List.of(projectEntity1, projectEntity2));
        given(projectRepository.findAllByUserEntityEmailAndIsDeletedFalse(param.toPageable(), email)).willReturn(projects);

        ProjectDate projectDate1 = new ProjectDate();
        ProjectDate projectDate2 = new ProjectDate();
        given(projectMapper.toDate(projectEntity1)).willReturn(projectDate1);
        given(projectMapper.toDate(projectEntity2)).willReturn(projectDate2);

        Page<ProjectDate> result = projectService.getProjectDate(param, email, role);

        assertThat(result).isNotNull();
        then(projectRepository).should().findAllByUserEntityEmailAndIsDeletedFalse(param.toPageable(), email);
    }


    @Test
    void createProject_생성_성공() {
        ProjectDTO.ProjectCreate create = new ProjectDTO.ProjectCreate(
                "프로젝트1",
                "프로젝트 설명1",
                "https://example.com/project1.jpg",
                List.of(1L, 2L),
                LocalDateTime.of(2024, 11, 21, 0, 0, 0),
                LocalDateTime.of(2025, 11, 21, 0, 0, 0)
        );

        given(userRepository.findByEmailAndIsDeleteFalse(email)).willReturn(Optional.of(userEntity));

        given(optionRepository.findById(1L)).willReturn(Optional.of(optionEntity1));
        given(optionRepository.findById(2L)).willReturn(Optional.of(optionEntity2));

        given(optionRepository.findByIdIn(create.getOptionIds())).willReturn(List.of(optionEntity1, optionEntity2));


        given(projectMapper.toEntity(any(ProjectDTO.ProjectCreate.class), any(UserEntity.class), anyList())).willReturn(projectEntity1);
        given(projectRepository.save(projectEntity1)).willReturn(projectEntity1);

        ProjectDTO.ProjectDetail projectDetail = new ProjectDTO.ProjectDetail(
                1L,
                "프로젝트1",
                "프로젝트 설명1",
                "https://example.com/project1.jpg",
                LocalDateTime.of(2024, 11, 21, 0, 0, 0),
                LocalDateTime.of(2025, 11, 21, 0, 0, 0),
                List.of(1L, 2L),
                0,
                null
        );
        given(projectMapper.toDetail(projectEntity1)).willReturn(projectDetail);

        ProjectDTO.ProjectDetail result = projectService.createProject(create, email, role);

        assertThat(result).isEqualTo(projectDetail);
        then(userRepository).should().findByEmailAndIsDeleteFalse(email);
        then(optionRepository).should().findByIdIn(create.getOptionIds());
        then(projectRepository).should().save(projectEntity1);
    }

    @Test
    void 프로젝트_수정_검증() {
        ProjectDTO.ProjectUpdate update = new ProjectDTO.ProjectUpdate(
                "프로젝트2",
                "프로젝트 설명2",
                "https://example.com/project2.jpg",
                List.of(1L, 2L),
                LocalDateTime.of(2024, 11, 21, 0, 0, 0),
                LocalDateTime.of(2025, 11, 21, 0, 0, 0)
        );

        given(optionRepository.findById(1L)).willReturn(Optional.of(optionEntity1));
        given(optionRepository.findById(2L)).willReturn(Optional.of(optionEntity2));

        given(optionRepository.findByIdIn(update.getOptionIds())).willReturn(List.of(optionEntity1, optionEntity2));

        ProjectDTO.ProjectDetail projectDetail = new ProjectDTO.ProjectDetail(
                1L,
                "프로젝트2",
                "프로젝트 설명2",
                "https://example.com/project2.jpg",
                LocalDateTime.of(2024, 11, 21, 0, 0, 0),
                LocalDateTime.of(2025, 11, 21, 0, 0, 0),
                List.of(1L, 2L),
                0,
                null
        );

        given(projectRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(projectEntity1));

        given(projectMapper.toUpdate(any(ProjectEntity.class), any(ProjectDTO.ProjectUpdate.class), anyList())).willReturn(projectEntity2);

        given(projectMapper.toDetail(any(ProjectEntity.class))).willReturn(projectDetail);

        ProjectDTO.ProjectDetail result = projectService.updateProject(1L, update, email, role);

        assertThat(result).isEqualTo(projectDetail);
        assertThat(result.getName()).isEqualTo("프로젝트2");
        assertThat(result.getDescription()).isEqualTo("프로젝트 설명2");
        assertThat(result.getImageURL()).isEqualTo("https://example.com/project2.jpg");
        then(projectOptionRepository).should().deleteByProjectEntity(projectEntity1);
        then(optionRepository).should().findByIdIn(update.getOptionIds());
    }

    @Test
    void 프로젝트_삭제_검증() {

        long projectId = 1L;

        projectEntity1.setId(projectId);
        projectEntity1.setIsDeleted(false);

        given(projectRepository.findByIdAndIsDeletedFalse(projectId)).willReturn(Optional.of(projectEntity1));

        Long result = projectService.deleteProject(projectId, email, role);

        assertThat(result).isEqualTo(projectId);
        assertThat(projectEntity1.getIsDeleted()).isTrue();
        then(projectRepository).should().findByIdAndIsDeletedFalse(projectId);
    }

}
