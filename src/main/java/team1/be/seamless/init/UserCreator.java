package team1.be.seamless.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team1.be.seamless.dto.UserDTO.UserSimple;
import team1.be.seamless.service.UserService;

@Component
public class UserCreator {

    private final UserService userService;

    @Autowired
    public UserCreator(UserService userService) {
        this.userService = userService;
    }

    public void creator() {
        userService.createUser(new UserSimple("사용자1", "user1@google.com", "user1Image.jpg"));
    }
}
