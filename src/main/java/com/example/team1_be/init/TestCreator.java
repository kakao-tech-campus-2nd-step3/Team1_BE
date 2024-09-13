package com.example.team1_be.init;

import com.example.team1_be.DTO.TestDTO.create;
import com.example.team1_be.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestCreator {

    private final TestService testService;

    @Autowired
    public TestCreator(TestService testService) {
        this.testService = testService;
    }

    public void creator() {
        testService.createTest(new create("name1"));
        testService.createTest(new create("name2"));
        testService.createTest(new create("name3"));
    }
}
