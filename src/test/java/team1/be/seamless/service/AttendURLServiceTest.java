package team1.be.seamless.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.entity.enums.Role;
import team1.be.seamless.repository.ProjectRepository;
import team1.be.seamless.util.auth.AesEncrypt;
import team1.be.seamless.util.errorException.BaseHandler;

class AttendURLServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private AesEncrypt aesEncrypt;

    @InjectMocks
    private AttendURLService attendURLService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
    }

    @Test // 참여 URL이 정상적으로 생성되는지 검증
    void 참여_URL_생성_성공() {
        String email = "test@example.com";
        String role = Role.USER.toString();
        Long projectId = 1L;

        ProjectEntity project = new ProjectEntity();
        project.setId(projectId);
        project.setEndDate(LocalDateTime.now().plusDays(10));

        when(projectRepository.findByIdAndUserEntityEmailAndIsDeletedFalse(projectId, email))
                .thenReturn(Optional.of(project));
        when(aesEncrypt.encrypt(anyString())).thenReturn("encryptedURL");

        String result = attendURLService.generateAttendURL(email, role, projectId);

        assertNotNull(result);
        assertEquals("encryptedURL", result);
        verify(projectRepository, times(1)).findByIdAndUserEntityEmailAndIsDeletedFalse(projectId, email);
        verify(aesEncrypt, times(1)).encrypt(anyString());
    }

    @Test // 프로젝트가 존재하지 않을 때 예외가 발생하는지 검증
    void 프로젝트_존재하지_않음_예외() {

        String email = "test@example.com";
        String role = Role.USER.toString();
        Long projectId = 1L;

        when(projectRepository.findByIdAndUserEntityEmailAndIsDeletedFalse(projectId, email))
                .thenReturn(Optional.empty());

        BaseHandler exception = assertThrows(BaseHandler.class,
                () -> attendURLService.generateAttendURL(email, role, projectId));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode().value());
        assertEquals("프로젝트가 존재하지 않음", exception.getReason());
    }

    @Test // 프로젝트가 종료된 경우 예외가 발생하는지 검증
    void 프로젝트_종료됨_예외() {

        String email = "test@example.com";
        String role = Role.USER.toString();
        Long projectId = 1L;

        ProjectEntity project = new ProjectEntity();
        project.setId(projectId);
        project.setEndDate(LocalDateTime.now().minusDays(1)); // 종료 설정임ㅇㅇ

        when(projectRepository.findByIdAndUserEntityEmailAndIsDeletedFalse(projectId, email))
                .thenReturn(Optional.of(project));

        BaseHandler exception = assertThrows(BaseHandler.class,
                () -> attendURLService.generateAttendURL(email, role, projectId));

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode().value());
        assertEquals("프로젝트는 종료되었습니다.", exception.getReason());
    }

    @Test // 권한이 없는 경우 예외가 발생하는지 검증
    void 권한_없음_예외() {

        String email = "test@example.com";
        String role = Role.MEMBER.toString(); // 팀원은 노권한임
        Long projectId = 1L;

        ProjectEntity project = new ProjectEntity();
        project.setId(projectId);
        project.setEndDate(LocalDateTime.now().plusDays(10));

        when(projectRepository.findByIdAndUserEntityEmailAndIsDeletedFalse(projectId, email))
                .thenReturn(Optional.of(project));

        BaseHandler exception = assertThrows(BaseHandler.class,
                () -> attendURLService.generateAttendURL(email, role, projectId));

        assertEquals(HttpStatus.UNAUTHORIZED.value(), exception.getStatusCode().value());
        assertEquals("생성 권한이 없습니다.", exception.getReason());
    }
}
