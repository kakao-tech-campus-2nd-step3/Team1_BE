package com.example.team1_be.mapper;

import com.example.team1_be.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(String userName, String email, String picture) {
        return new UserEntity(userName, email, picture);
    }
}
