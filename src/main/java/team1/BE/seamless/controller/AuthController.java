package team1.BE.seamless.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team1.BE.seamless.service.AuthService;
import team1.BE.seamless.util.auth.Token;
import team1.BE.seamless.util.page.SingleResult;

@Tag(name = "인증기능 구현")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "인증 코드로 멤버 토큰 반환")
    @GetMapping("/memberCode")
    public SingleResult<Token> memberCodeJoin(@Valid @RequestParam String memberCode) {
        return new SingleResult<>(authService.memberCodeJoin(memberCode));
    }

    @Operation(summary = "인증 코드 생성(테스트용)")
    @GetMapping("/memberCode/create")
    public SingleResult<String> memberCodeCreate(@Valid @RequestParam String memberCode) {
        return new SingleResult<>(authService.memberCodeCreate(memberCode));
    }
}
