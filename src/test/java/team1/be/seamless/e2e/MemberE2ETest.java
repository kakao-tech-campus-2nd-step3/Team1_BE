package team1.be.seamless.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import team1.be.seamless.entity.MemberEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberE2ETest {

    @LocalServerPort
    private int port;
    private String baseUrl;
    private final TestRestTemplate restTemplate;
    private String adminToken;
    private String memberToken;
    private HttpHeaders headers = new HttpHeaders();

    @Autowired
    public MemberE2ETest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/project/1/member";

        CreateMember member = new CreateMember(
                "ex@gmail.com",
                "sv_XKCT5j5Sm0msQw-mEAmstJ5tq9uBh6c8_QLhzKGo=",
                "랄랄랄"
        );
        HttpEntity<CreateMember> request1 = new HttpEntity<>(member);
        ResponseEntity<String> response1 = restTemplate.exchange(
                baseUrl,
                POST,
                request1,
                String.class
        );

        int startIndex = response1.getBody().indexOf("\"attendURL\":\"") + "\"attendURL\":\"".length();
        int endIndex = response1.getBody().indexOf("\"", startIndex);


        String code = response1.getBody().substring(startIndex, endIndex);

        HttpEntity<Void> request2 = new HttpEntity<>(null);
        ResponseEntity<String> response2 = restTemplate.exchange(
                "http://localhost:" + port + "/api/auth/memberCode?memberCode=" + code,
                GET,
                request2,
                String.class
        );

        startIndex = response2.getBody().indexOf("\"token\":\"") + "\"token\":\"".length();
        endIndex = response2.getBody().indexOf("\"", startIndex);

        memberToken = response2.getBody().substring(startIndex, endIndex);


        HttpEntity<Void> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/api/test/userToken/1",
                POST,
                requestEntity,
                String.class
        );
        startIndex = responseEntity.getBody().indexOf("\"token\":\"") + "\"token\":\"".length();
        endIndex = responseEntity.getBody().indexOf("\"", startIndex);

        adminToken = responseEntity.getBody().substring(startIndex, endIndex);

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);
    }

    @DisplayName("\"멤버 이메일 빈 칸으로 정보 수정시 반영이 안되거나 예외처리 되는가?\" 에 대한 테스트")
    @Test
    void 멤버_정보_수정_테스트() {
        UpdateMember updateInfo = new UpdateMember(
                "새로운 이름",
                "팀원",
                "",
                "http://example.com/"
        );
        HttpEntity<UpdateMember> requestEntity = new HttpEntity<>(updateInfo, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/1",
                PUT,
                requestEntity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @DisplayName("멤버가 멤버를 삭제 가능한지에 대한 테스트")
    @Test
    void 멤버_삭제권한_테스트() {
        headers.setBearerAuth(memberToken);


        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/1",
                DELETE,
                requestEntity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
    }

    @DisplayName("\"멤버 삭제(softdelete로 되는가, 다시 조회하면 조회 되는가)\" 에 대한 테스트")
    @Test
    void 멤버_삭제_후_조회_여부_테스트() {
        headers.setBearerAuth(adminToken);


        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response1 = restTemplate.exchange(
                baseUrl + "/2",
                DELETE,
                requestEntity,
                String.class
        );
        assertThat(response1.getStatusCode()).isEqualTo(OK);

        ResponseEntity<MemberEntity> response2 = restTemplate.exchange(
                baseUrl + "2",
                GET,
                new HttpEntity<>(headers),
                MemberEntity.class
        );
        assertThat(response2.getStatusCode()).isEqualTo(NOT_FOUND);
    }

}