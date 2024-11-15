package team1.be.seamless.mapper;

import org.junit.jupiter.api.Test;
import team1.be.seamless.dto.UserDTO;
import team1.be.seamless.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    private UserMapper userMapper = new UserMapper();


    @Test
    void 유저엔티티생성() {
        String userName = "test";
        String eamil = "test@test.com";
        String picture = "https://img.com";

        UserEntity userEntity = userMapper.toEntity(userName, eamil, picture);

        assertEquals(userEntity.getName(), userName);
        assertEquals(userEntity.getEmail(), eamil);
        assertEquals(userEntity.getPicture(), picture);

    }

    @Test
    void 유저업데이트생성() {

        String userName = "test";
        String eamil = "test@test.com";
        String picture = "https://img.com";

        String updateUserName = "test2";
        String updatePicture = "https://img2.com";

        UserEntity userEntity = userMapper.toEntity(userName, eamil, picture);
        UserDTO.UserUpdate update = new UserDTO.UserUpdate(updateUserName, updatePicture);

        UserEntity updateUser = userMapper.toUpdate(userEntity, update);

        assertEquals(updateUser.getName(), updateUserName);
        assertEquals(updateUser.getEmail(), eamil);
        assertEquals(updateUser.getPicture(), updatePicture);
    }

    @Test
    void 유저심플생성() {
        String userName = "test";
        String eamil = "test@test.com";
        String picture = "https://img.com";

        UserEntity userEntity = userMapper.toEntity(userName, eamil, picture);
        UserDTO.UserSimple simple = userMapper.toUserSimple(userEntity);

        assertEquals(simple.getUsername(), userName);
        assertEquals(simple.getEmail(), eamil);
        assertEquals(simple.getPicture(), picture);

    }

    @Test
    void 유저디테일생성() {
        String userName = "test";
        String eamil = "test@test.com";
        String picture = "https://img.com";

        UserEntity userEntity = userMapper.toEntity(userName, eamil, picture);
        UserDTO.UserDetails details = userMapper.toUserDetails(userEntity);

        assertEquals(details.getUsername(), userName);
        assertEquals(details.getEmail(), eamil);
        assertEquals(details.getPicture(), picture);
    }
}