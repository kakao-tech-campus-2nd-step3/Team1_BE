package com.example.team1_be.service;

import com.example.team1_be.DTO.UserDTO.UserDetails;
import com.example.team1_be.entity.UserEntity;
import com.example.team1_be.mapper.UserMapper;
import com.example.team1_be.repository.UserRepository;
import com.example.team1_be.util.auth.ParsingPram;
import com.example.team1_be.util.errorException.BaseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ParsingPram parsingPram;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper,
        ParsingPram parsingPram) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.parsingPram = parsingPram;
    }

    public UserDetails getUser(HttpServletRequest req) {
        UserEntity user = userRepository.findByEmail(parsingPram.getEmail(req))
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."));
        return userMapper.toUserDetails(user);
    }

}
