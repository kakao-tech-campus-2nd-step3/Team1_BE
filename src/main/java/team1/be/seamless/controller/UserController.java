package team1.be.seamless.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team1.be.seamless.dto.UserDTO.UserDetails;
import team1.be.seamless.dto.UserDTO.UserSimple;
import team1.be.seamless.dto.UserDTO.UserUpdate;
import team1.be.seamless.service.UserService;
import team1.be.seamless.util.auth.ParsingParam;
import team1.be.seamless.util.page.SingleResult;

@Tag(name = "유저 구현")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final ParsingParam parsingParam;

    @Autowired
    public UserController(UserService userService, ParsingParam parsingParam) {
        this.userService = userService;
        this.parsingParam = parsingParam;
    }

    @Operation(summary = "유저 정보 조회")
    @GetMapping
    public SingleResult<UserDetails> getUser(HttpServletRequest req) {
        return new SingleResult<>(userService.getUser(parsingParam.getEmail(req), parsingParam.getRole(req)));
    }

    @Operation(summary = "유저 정보 수정")
    @PutMapping
    public SingleResult<UserSimple> updateUser(HttpServletRequest req, @Valid @RequestBody
    UserUpdate update) {
        return new SingleResult<>(userService.updateUser(parsingParam.getEmail(req), parsingParam.getRole(req), update));
    }

    @Operation(summary = "유저 정보 삭제")
    @DeleteMapping
    public SingleResult<UserSimple> deleteUser(HttpServletRequest req) {
        return new SingleResult<>(userService.deleteUser(parsingParam.getEmail(req), parsingParam.getRole(req)));
    }

}
