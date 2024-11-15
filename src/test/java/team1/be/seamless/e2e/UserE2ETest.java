package team1.be.seamless.e2e;

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
import org.springframework.test.annotation.DirtiesContext;
import team1.be.seamless.dto.UserDTO.UserUpdate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserE2ETest {

    @LocalServerPort
    private int port;
    private String url = "http://localhost:";
    private final TestRestTemplate restTemplate;
    private String token;
    private HttpHeaders headers = new HttpHeaders();


    @Autowired
    public UserE2ETest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 특정 유저id의 토큰 파싱
     */
    @BeforeEach
    public void setUp() {
        HttpEntity<Long> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url + port + "/api/test/userToken/2",
                POST,
                requestEntity, String.class);

        int startIndex = responseEntity.getBody().indexOf("\"token\":\"") + "\"token\":\"".length();
        int endIndex = responseEntity.getBody().indexOf("\"", startIndex);

        token = responseEntity.getBody().substring(startIndex, endIndex);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
    }

    @Test
    void 유저정보_수정_테스트_성공() {
        UserUpdate body = new UserUpdate("name1", "https://tests.com/qwer1234");

        HttpEntity<Long> requestEntity = new HttpEntity(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/user",
                PUT,
                requestEntity, String.class);

        System.out.println(responseEntity);
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void 유저정보_수정_테스트_실패_url형식오류() {
        UserUpdate body = new UserUpdate("name1", "qwer1234");

        HttpEntity<Long> requestEntity = new HttpEntity(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/user",
                PUT,
                requestEntity, String.class);

        System.out.println(responseEntity);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void 유저정보_삭제_성공() {
        HttpEntity<Long> requestEntity = new HttpEntity(null, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/user",
                DELETE,
                requestEntity, String.class);

        System.out.println(responseEntity);
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    @DirtiesContext
    @Test
    void 유저정보_삭제_실패() {
        HttpEntity<Long> requestEntity = new HttpEntity(null, headers);
        restTemplate.exchange(url + port + "/api/user",
                DELETE,
                requestEntity, String.class);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/user",
                DELETE,
                requestEntity, String.class);

        System.out.println(responseEntity);
        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }
}