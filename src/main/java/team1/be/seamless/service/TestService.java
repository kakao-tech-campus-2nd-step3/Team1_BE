package team1.be.seamless.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import team1.be.seamless.dto.TestDTO.create;
import team1.be.seamless.dto.TestDTO.getList;
import team1.be.seamless.entity.TestEntity;
import team1.be.seamless.entity.UserEntity;
import team1.be.seamless.mapper.TestMapper;
import team1.be.seamless.repository.TestRepository;
import team1.be.seamless.repository.UserRepository;
import team1.be.seamless.util.auth.JwtToken;
import team1.be.seamless.util.auth.Token;
import team1.be.seamless.util.errorException.BaseHandler;

@Service
public class TestService {

    private final TestMapper testMapper;
    private final TestRepository testRepository;
    private final JwtToken jwtToken;
    private final UserRepository userRepository;

    @Autowired
    public TestService(TestMapper testMapper, TestRepository testRepository, JwtToken jwtToken,
        UserRepository userRepository) {
        this.testMapper = testMapper;
        this.testRepository = testRepository;
        this.jwtToken = jwtToken;
        this.userRepository = userRepository;
    }

    public Page<TestEntity> getTestList(@Valid getList param) {
        return testRepository.findAll(param.toPageable());
    }

    @Profile("test")
    public TestEntity getTest(long get) {
        TestEntity entity = testRepository.findById(get)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않음"));
        System.out.println(entity);
        return entity;
    }

    @Profile("test")
    public TestEntity createTest(@Valid create create) {
        TestEntity entity = new TestEntity(create.getName());
        System.out.println(entity);
        testRepository.save(entity);
        return entity;
    }

    @Profile("test")
    public Token TestTokenCreate(Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.FORBIDDEN, "유저 없음"));

        String token = jwtToken.createUserToken(user);

        return new Token(token);
    }
}
