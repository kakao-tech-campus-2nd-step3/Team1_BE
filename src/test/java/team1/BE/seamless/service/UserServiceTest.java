package team1.BE.seamless.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

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
class UserServiceTest {

    @LocalServerPort
    private int port;
    private String url = "http://localhost:";
    private final TestRestTemplate restTemplate;
    private String token;
    private HttpHeaders headers = new HttpHeaders();

    private final UserService userService;


    @Autowired
    public UserServiceTest(TestRestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
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
    void 유저정보_수정_테스트_실패_url() {
        UserUpdate body = new UserUpdate("name1","qwer1234");

        HttpEntity<Long> requestEntity = new HttpEntity(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/user",
            PUT,
            requestEntity, String.class);

        System.out.println(responseEntity);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void 유저정보_삭제_실패() {
        HttpEntity<Long> requestEntity = new HttpEntity(null, headers);
//        삭제 두 번 시도
        restTemplate.exchange(url + port + "/api/user",
            DELETE,
            requestEntity, String.class);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/user",
            DELETE,
            requestEntity, String.class);

        System.out.println(responseEntity);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }
}