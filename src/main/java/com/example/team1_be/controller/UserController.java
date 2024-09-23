package com.example.team1_be.controller;

import com.example.team1_be.DTO.AuthDTO;
import com.example.team1_be.DTO.UserDTO.UserDetails;
import com.example.team1_be.entity.UserEntity;
import com.example.team1_be.service.UserService;
import com.example.team1_be.util.page.SingleResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증기능 구현")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public SingleResult<UserDetails> getUser(HttpServletRequest req) {
        return new SingleResult<>(userService.getUser(req));
    }

    @PutMapping
    public SingleResult<UserDetails> updateUser(HttpServletRequest req) {
        return new SingleResult<>(userService.getUser(req));
    }

}
