package team1.BE.seamless.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.GET;
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

import team1.BE.seamless.DTO.MemberRequestDTO.UpdateMember;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.service.MemberService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberServiceTest {

    @LocalServerPort
    private int port;
    private String url = "http://localhost:";
    private final TestRestTemplate restTemplate;
    private String token;
    private HttpHeaders headers = new HttpHeaders();
    private final MemberService memberService;

    @Autowired
    public MemberServiceTest(TestRestTemplate restTemplate, MemberService memberService) {
        this.restTemplate = restTemplate;
        this.memberService = memberService;
    }

    @BeforeEach
    public void setUp() {
        HttpEntity<Long> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url + port + "/api/test/userToken/1",
                PUT,
                requestEntity, String.class);

        int startIndex = responseEntity.getBody().indexOf("\"token\":\"") + "\"token\":\"".length();
        int endIndex = responseEntity.getBody().indexOf("\"", startIndex);
        token = responseEntity.getBody().substring(startIndex, endIndex);

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        this.url += port + "/api/project/1/member/";
    }


    //"멤버 이메일 빈 칸으로 정보 수정시 반영이 안되거나 예외처리 되는가?" 에 대한 테스트
    @Test
    void updateMemberWithEmptyEmailShouldFail() {
        UpdateMember updateInfo = new UpdateMember("새로운 이름", "팀원", "", "http://example.com/");
        HttpEntity<UpdateMember> requestEntity = new HttpEntity<>(updateInfo, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url + "1", // 테스트 Member ID
                PUT,
                requestEntity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    }



    // "멤버 삭제(softdelete로 되는가, 다시 조회하면 조회 되는가)" 에 대한 테스트
    @Test
    void softDeleteMemberAndRequery() {
        // Soft delete member
        restTemplate.exchange(url + "1", // 테스트 Member ID
                PUT,
                new HttpEntity<>(new UpdateMember("", "팀원", "email@example.com", "http://example.com", true), headers),
                Void.class);

        ResponseEntity<MemberEntity> response = restTemplate.exchange(
                url + "123",
                GET,
                new HttpEntity<>(headers),
                MemberEntity.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }
}
