package com.example.team1_be.controller;

import com.example.team1_be.DTO.UserDTO.UserDetails;
import com.example.team1_be.DTO.UserDTO.UserSimple;
import com.example.team1_be.DTO.UserDTO.UserUpdate;
import com.example.team1_be.service.UserService;
import com.example.team1_be.util.page.SingleResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public SingleResult<UserSimple> updateUser(HttpServletRequest req, @Valid @RequestBody
        UserUpdate update) {
        return new SingleResult<>(userService.updateUser(req,update));
    }

}
