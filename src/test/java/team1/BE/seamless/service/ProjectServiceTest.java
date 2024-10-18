package team1.BE.seamless.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
import team1.BE.seamless.DTO.UserDTO.UserUpdate;

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
}