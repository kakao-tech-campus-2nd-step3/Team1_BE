package team1.BE.seamless.controller;

import team1.BE.seamless.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증기능 구현")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

//    @GetMapping("/login/oauth2/code/google")
//    public SingleResult<Token> login(OAuth2AuthenticationToken authenticationToken) {
//        return new SingleResult<>(null);
//    }
}
