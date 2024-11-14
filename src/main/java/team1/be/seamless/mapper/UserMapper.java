package team1.be.seamless.mapper;

import org.springframework.stereotype.Component;
import team1.be.seamless.dto.UserDTO.UserDetails;
import team1.be.seamless.dto.UserDTO.UserSimple;
import team1.be.seamless.dto.UserDTO.UserUpdate;
import team1.be.seamless.entity.UserEntity;
import team1.be.seamless.util.Util;

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
