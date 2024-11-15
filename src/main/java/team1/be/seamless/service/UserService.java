package team1.be.seamless.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team1.be.seamless.dto.UserDTO.UserDetails;
import team1.be.seamless.dto.UserDTO.UserSimple;
import team1.be.seamless.dto.UserDTO.UserUpdate;
import team1.be.seamless.entity.UserEntity;
import team1.be.seamless.entity.enums.Role;
import team1.be.seamless.mapper.UserMapper;
import team1.be.seamless.repository.UserRepository;
import team1.be.seamless.util.errorException.BaseHandler;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDetails getUser(String email, String role) {
        if (!Role.USER.isRole(role)) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED, "조회 권한이 없습니다.");
        }
        UserEntity user = userRepository.findByEmailAndIsDeleteFalse(email)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."));
        return userMapper.toUserDetails(user);
    }

    @Transactional
    public UserSimple updateUser(String email, String role, UserUpdate update) {
        if (!Role.USER.isRole(role)) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED, "조회 권한이 없습니다.");
        }

        UserEntity user = userRepository.findByEmailAndIsDeleteFalse(email)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."));

        userMapper.toUpdate(user, update);

        return userMapper.toUserSimple(user);
    }

    @Transactional
    public UserSimple deleteUser(String email, String role) {
        if (!Role.USER.isRole(role)) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED, "조회 권한이 없습니다.");
        }

        UserEntity user = userRepository.findByEmailAndIsDeleteFalse(email)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."));

        user.setIsDelete();

        return userMapper.toUserSimple(user);
    }

    @Profile("test")
    @Transactional
    public UserEntity createUser(UserSimple simple) {
        return userRepository.findByEmailAndIsDeleteFalse(simple.getEmail())
                .orElseGet(() -> userRepository.save(
                        userMapper.toEntity(
                                simple.getUsername(),
                                simple.getEmail(),
                                simple.getPicture()
                        )));
    }
}
