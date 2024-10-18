package team1.BE.seamless;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import team1.BE.seamless.DTO.ProjectDTO.ProjectCreate;
import team1.BE.seamless.DTO.ProjectDTO.ProjectUpdate;
import team1.BE.seamless.service.ProjectService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectServiceTest {
    @LocalServerPort
    private int port;
    private String url = "http://localhost:";
    private final TestRestTemplate restTemplate;
    private String token;
    private HttpHeaders headers = new HttpHeaders();
    private final ProjectService projectService;
    @Autowired
    public ProjectServiceTest(TestRestTemplate restTemplate, ProjectService projectService) {
        this.restTemplate = restTemplate;
        this.projectService = projectService;
    }
    /**
     * 특정 유저id의 토큰 파싱
     */
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
    void 프로젝트_생성_성공() {
        ProjectCreate body = new ProjectCreate("프로젝트 이름2",
            LocalDateTime.of(2024, 11, 1, 1, 1, 1),
            LocalDateTime.of(2024, 11, 4, 4, 4, 4),
            List.of(2L, 3L));
        HttpEntity<ProjectUpdate> requestEntity = new HttpEntity(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/project",
            POST,
            requestEntity,
            String.class);
        System.out.println(responseEntity.getBody());
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void 프로젝트_날짜_수정_성공() {
        ProjectUpdate body = new ProjectUpdate("프로젝트 이름1", List.of(1L, 2L, 3L),
            LocalDateTime.of(2024, 10, 1, 0, 0, 0),
            LocalDateTime.of(2024, 10, 4, 4, 4, 4));
        HttpEntity<ProjectUpdate> requestEntity = new HttpEntity(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/project/1",
            PUT,
            requestEntity,
            String.class);
        System.out.println(responseEntity.getBody());
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    /**
     *
     *  이 부분 전에 ProjectDTO에서 startDate, endDate 생성 시 현재 이전 값도 되야 되지 않나?
     *  라고 말했던 적 있어서 일단 만들어 놓고 주석처리
     *  현재는 @Future가 설정 안되어 잇어서 과거의 값도 생성 가능한 상태
     */
//    @Test
//    void 프로젝트_날짜_현재_이전_불가() {
//        // 현재보다 이전인 날짜 설정
//        ProjectUpdate body = new ProjectUpdate("프로젝트 이름", List.of(1L, 2L, 3L),
//            LocalDateTime.of(2022, 10, 1, 0, 0),
//            LocalDateTime.of(2022, 10, 5, 0, 0));
//
//        HttpEntity<ProjectUpdate> requestEntity = new HttpEntity<>(body, headers);
//        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/project/1",
//            PUT, requestEntity, String.class);
//
//        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
//    }

    @Test
    void 프로젝트_종료일_시작일_이전_불가() {
        // 종료일이 시작일보다 이전인 날짜 설정
        ProjectUpdate body = new ProjectUpdate("프로젝트 이름", List.of(1L, 2L, 3L),
            LocalDateTime.of(2024, 10, 10, 0, 0),
            LocalDateTime.of(2024, 10, 5, 0, 0));

        HttpEntity<ProjectUpdate> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/project/1",
            PUT, requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void 프로젝트_생성시_시작일과_종료일_간견은_1일_이상_필수() {
        // 1일보다 작은 갭을 설정
        ProjectUpdate body = new ProjectUpdate("프로젝트 이름", List.of(1L, 2L, 3L),
            LocalDateTime.of(2024, 10, 10, 0, 0),
            LocalDateTime.of(2024, 10, 10, 23, 59));

        HttpEntity<ProjectUpdate> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/project/1",
            PUT, requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    /**
    * 이 부분도 지금은 그냥 Service가 delete시 isDeleted = true로 변경하고
     * 그냥 id를 반환하는 구조인데, 이를 체크하기 위해서 isDeleted를 포함한
     * DTO를 반환하는 방법은 좋지 않은 것 같아서 일단을 이렇게 구현*/
    @Test
    void 삭제된_프로젝트_조회_불가() {
        HttpEntity<Long> requestEntity = new HttpEntity(null, headers);

        restTemplate.exchange(url + port + "/api/project/1",
            DELETE,
            requestEntity, String.class);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/project/1",
            DELETE,
            requestEntity, String.class);

        System.out.println(responseEntity);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }


}



