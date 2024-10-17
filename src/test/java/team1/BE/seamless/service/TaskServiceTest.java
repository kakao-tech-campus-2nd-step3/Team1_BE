package team1.BE.seamless.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import team1.BE.seamless.DTO.ProjectDTO.ProjectDetail;
import team1.BE.seamless.DTO.TaskDTO.Create;
import team1.BE.seamless.DTO.TaskDTO.TaskDetail;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.TaskEntity;
import team1.BE.seamless.repository.MemberRepository;
import team1.BE.seamless.repository.ProjectRepository;
import team1.BE.seamless.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskServiceTest {

    @LocalServerPort
    private int port;
    private String url = "http://localhost:";
    private final TestRestTemplate restTemplate;
    private String token;
    private HttpHeaders headers = new HttpHeaders();

    private TaskService taskService;
    private ProjectService projectService;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private MemberRepository memberRepository;

    @Autowired
    public TaskServiceTest(TestRestTemplate restTemplate, TaskService taskService, ProjectRepository projectRepository, UserRepository userRepository, MemberRepository memberRepository, ProjectService projectService) {
        this.restTemplate = restTemplate;
        this.taskService = taskService;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.projectService = projectService;
    }

    @BeforeEach
    public void setUp() {
        HttpEntity<Long> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + "/api/test/userToken/1",
            POST,
            requestEntity, String.class);

        int startIndex = responseEntity.getBody().indexOf("\"token\":\"") + "\"token\":\"".length();
        int endIndex = responseEntity.getBody().indexOf("\"", startIndex);

        token = responseEntity.getBody().substring(startIndex, endIndex);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
    }

    @Test
    public void 태스크_시작_시간이_프로젝트_일정_범위보다_이를_경우_실패() {
        Create body = new Create("태스크1", "첫번째 태스크입니다.", 1L, LocalDateTime.of(2001, 10, 10, 0, 0), LocalDateTime.of(2025, 5, 3, 1, 0, 0));

        HttpEntity<Long> requestEntity = new HttpEntity(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/project/1/task", POST, requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void 태스크_마감_시간이_프로젝트_일정_범위보다_늦을_경우_실패() {
        Create body = new Create("태스크1", "첫번째 태스크입니다.", 1L, LocalDateTime.of(2024, 12, 1, 0, 0), LocalDateTime.of(2100, 5, 3, 1, 0, 0));

        HttpEntity<Long> requestEntity = new HttpEntity(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/project/1/task", POST, requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void 프로젝트_삭제시_태스크_조회_실패() {
        // 프로젝트 삭제
        projectService.deleteProject(1L);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.getTask(1L);
        });

        HttpEntity<Long> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/project/task/1", GET, requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }
}