package com.example.team1_be.mapper;

import com.example.team1_be.DTO.UserDTO.UserDetails;
import com.example.team1_be.DTO.UserDTO.UserSimple;
import com.example.team1_be.DTO.UserDTO.UserUpdate;
import com.example.team1_be.entity.UserEntity;
import com.example.team1_be.util.Util;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(String userName, String email, String picture) {
        return new UserEntity(userName, email, picture);
    }

    public UserEntity toUpdate(UserEntity entity, UserUpdate update) {

        return entity.update(
            Util.isNull(update.getUsername()) ? entity.getName() : update.getUsername(),
            Util.isNull(update.getPicture()) ? entity.getPicture() : update.getPicture()
        );
    }

    public UserSimple toUserSimple(UserEntity entity) {
        return new UserSimple(
            entity.getName(),
            entity.getEmail(),
            entity.getPicture()
        );
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
