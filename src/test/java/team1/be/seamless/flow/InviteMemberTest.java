package team1.be.seamless.flow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
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
import team1.be.seamless.dto.MemberRequestDTO.CreateMember;
import team1.be.seamless.dto.MemberRequestDTO.UpdateMember;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InviteMemberTest {

    @LocalServerPort
    private int port;
    private String url = "http://localhost:";
    private final TestRestTemplate restTemplate;
    private String token;
    private HttpHeaders headers = new HttpHeaders();

    @Autowired
    public InviteMemberTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    public void setUp() {
        this.url += port;

//        팀장의 토큰 요청
        HttpEntity<Long> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + "/api/test/userToken/1",
            POST,
            requestEntity, String.class);

        int startIndex = responseEntity.getBody().indexOf("\"token\":\"") + "\"token\":\"".length();
        int endIndex = responseEntity.getBody().indexOf("\"", startIndex);

        token = responseEntity.getBody().substring(startIndex, endIndex);

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
    }


    @Test
    void 멤버초대링크_멤버생성_멤버토큰받아오기() {
        HttpEntity<UpdateMember> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
            url + "/api/project/1/invite-link",
            POST,
            requestEntity,
            String.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);

        int startIndex =
            response.getBody().indexOf("\"resultData\":\"") + "\"resultData\":\"".length();
        int endIndex = response.getBody().indexOf("\"", startIndex);

        //        새로운 멤버 생성
        CreateMember member = new CreateMember("ex@gmail.com",
            response.getBody().substring(startIndex, endIndex), "qqq");
        HttpEntity<CreateMember> request1 = new HttpEntity<>(member);
        ResponseEntity<String> response1 = restTemplate.exchange(
            url + "/api/project/1/member",
            POST,
            request1, String.class);

        assertThat(response1.getStatusCode()).isEqualTo(OK);

        startIndex = response1.getBody().indexOf("\"attendURL\":\"") + "\"attendURL\":\"".length();
        endIndex = response1.getBody().indexOf("\"", startIndex);

//        멤버 생성시 반환되는 코드 추출
        String code = response1.getBody().substring(startIndex, endIndex);

//        코드로 멤버 토큰 요청
        HttpEntity<Long> request2 = new HttpEntity<>(null);
        ResponseEntity<String> response2 = restTemplate.exchange(
            url + "/api/auth/memberCode?memberCode=" + code,
            GET,
            request2, String.class);

        assertThat(response2.getStatusCode()).isEqualTo(OK);
    }

}
