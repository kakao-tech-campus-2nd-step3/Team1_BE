package com.example.team1_be.mapper;

import com.example.team1_be.DTO.UserDTO.UserDetails;
import com.example.team1_be.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(String userName, String email, String picture) {
        return new UserEntity(userName, email, picture);
    }

    public UserDetails toUserDetails(UserEntity entity) {
        return new UserDetails(
            entity.getName(),
            entity.getEmail(),
            entity.getPicture(),
            entity.getRole().toString(),
            entity.getCreatedAt());
    }
}
