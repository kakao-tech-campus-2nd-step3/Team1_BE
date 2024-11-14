package team1.be.seamless.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team1.be.seamless.entity.MemberEntity;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.entity.UserEntity;
import team1.be.seamless.entity.enums.Role;
import team1.be.seamless.util.auth.JwtToken;
import team1.be.seamless.util.auth.ParsingParam;

@SpringBootTest
class JwtTokenTest {

    private final JwtToken jwtToken;
    private final ParsingParam param;
    @Mock
    private HttpServletRequest request;

    @Autowired
    public JwtTokenTest(JwtToken jwtToken, ParsingParam param) {
        this.jwtToken = jwtToken;
        this.param = param;
    }

    @BeforeEach
    public void setUp() {
        request = mock(HttpServletRequest.class);
    }

    @Test
    void 생성된_토큰_권한_검사_user() {
//        given
        String name = "test";
        String email = "test@test.com";
        String image = "https://testImage.com";
        UserEntity user = new UserEntity(name,email,image);

//        when
        String token = jwtToken.createUserToken(user);
        lenient().when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

//        then
        assertEquals(param.getEmail(request), email);
        assertEquals(param.getRole(request), Role.USER.toString());
    }

    @Test
    void 생성된_토큰_권한_검사_member() {
//        given
        String name = "test";
        String role = "member";
        String email = "test@test.com";
        String image = "https://testImage.com";
        ProjectEntity project = new ProjectEntity();
        MemberEntity member = new MemberEntity(name,role,email,image,project);

//        when
        String token = jwtToken.createMemberToken(member);
        lenient().when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

//        then
        assertEquals(param.getEmail(request), email);
        assertEquals(param.getRole(request), Role.MEMBER.toString());
    }
}