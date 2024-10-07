package team1.BE.seamless.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team1.BE.seamless.DTO.TestDTO.create;
import team1.BE.seamless.service.TestService;

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
