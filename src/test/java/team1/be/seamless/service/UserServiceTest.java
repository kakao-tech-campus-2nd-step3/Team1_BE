package team1.be.seamless.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team1.be.seamless.dto.UserDTO.UserDetails;
import team1.be.seamless.dto.UserDTO.UserSimple;
import team1.be.seamless.dto.UserDTO.UserUpdate;
import team1.be.seamless.entity.UserEntity;
import team1.be.seamless.entity.enums.Role;
import team1.be.seamless.mapper.UserMapper;
import team1.be.seamless.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    private String username = "testUser";
    private String email = "test@test.com";
    private String image = "https://testImage.com";

    private UserEntity mockUser;

    @BeforeEach
    public void setUp() {
        mockUser = new UserEntity(username, email, image);

        lenient().when(userRepository.findByEmailAndIsDeleteFalse(email)).thenReturn(Optional.of(mockUser));
    }

    @Test
    void 유저조회_테스트() {

        UserDetails mockUserDetails = new UserDetails(username, email, image, Role.USER.toString(), LocalDateTime.now());

        when(userMapper.toUserDetails(mockUser)).thenReturn(mockUserDetails);
        UserDetails user = userService.getUser(email, Role.USER.toString());

        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(image, user.getPicture());
    }

    @Test
    void 유저수정_테스트() {

        String updatedUsername = "testUser2";
        String updatedImage = "https://testImage2.com";

        UserSimple mockUserSimple = new UserSimple(updatedUsername, email, updatedImage);
        UserUpdate update = new UserUpdate(updatedUsername, updatedImage);

        when(userMapper.toUserSimple(mockUser)).thenReturn(mockUserSimple);
        UserSimple user = userService.updateUser(email, Role.USER.toString(), update);

        assertEquals(updatedUsername, user.getUsername());
        assertEquals(updatedImage, user.getPicture());
    }

    @Test
    void 유저삭제_테스트() {

        UserSimple mockUserSimple = new UserSimple(username, email, image);

        when(userMapper.toUserSimple(mockUser)).thenReturn(mockUserSimple);
        UserSimple user = userService.deleteUser(email, Role.USER.toString());

        assertEquals(username, user.getUsername());
        assertEquals(image, user.getPicture());
    }
}
