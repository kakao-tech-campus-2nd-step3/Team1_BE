package com.example.team1_be.controller;

import com.example.team1_be.DTO.TestDTO;
import com.example.team1_be.entity.TestEntity;
import com.example.team1_be.service.TestService;
import com.example.team1_be.util.page.PageMapper;
import com.example.team1_be.util.page.PageResult;
import com.example.team1_be.util.page.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "참조")
@RestController
@RequestMapping("/api/test")
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @Operation(summary = "조회 참조")
    @GetMapping
    public PageResult<TestEntity> getTestList(@Valid TestDTO.getList param) {
        return PageMapper.toPageResult(testService.getTestList(param));
    }

    @Operation(summary = "조회 참조")
    @GetMapping("/{id}")
    public SingleResult<TestEntity> getTest(@Valid @PathVariable long id) {
        return new SingleResult<>(testService.getTest(id));
    }

    @Operation(summary = "생성 참조")
    @PostMapping
    public SingleResult<TestEntity> createTest(@Valid @RequestBody TestDTO.create create) {
        return new SingleResult<>(testService.createTest(create));
    }
}
