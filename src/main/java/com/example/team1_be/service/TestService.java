package com.example.team1_be.service;

import com.example.team1_be.DTO.TestDTO.create;
import com.example.team1_be.DTO.TestDTO.getList;
import com.example.team1_be.entity.TestEntity;
import com.example.team1_be.mapper.TestMapper;
import com.example.team1_be.repository.TestRepository;
import com.example.team1_be.util.errorException.BaseHandler;
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
