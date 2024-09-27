package team1.BE.seamless.service;

import team1.BE.seamless.DTO.TestDTO.create;
import team1.BE.seamless.DTO.TestDTO.getList;
import team1.BE.seamless.entity.TestEntity;
import team1.BE.seamless.mapper.TestMapper;
import team1.BE.seamless.repository.TestRepository;
import team1.BE.seamless.util.errorException.BaseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private final TestMapper testMapper;
    private final TestRepository testRepository;

    @Autowired
    public TestService(TestMapper testMapper, TestRepository testRepository) {
        this.testMapper = testMapper;
        this.testRepository = testRepository;
    }

    public Page<TestEntity> getTestList(@Valid getList param) {
        return testRepository.findAll(param.toPageable());
    }

    public TestEntity getTest(long get) {
        TestEntity entity = testRepository.findById(get)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않음"));
        System.out.println(entity);
        return entity;
    }

    public TestEntity createTest(@Valid create create) {
        TestEntity entity = new TestEntity(create.getName());
        System.out.println(entity);
        testRepository.save(entity);
        return entity;
    }
}
