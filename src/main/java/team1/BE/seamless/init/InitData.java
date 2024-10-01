package team1.BE.seamless.init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class InitData {

    private final TestCreator testCreator;
    private final ProjectCreator projectCreator;

    @Autowired
    public InitData(TestCreator testCreator, ProjectCreator projectCreator) {
        this.testCreator = testCreator;
        this.projectCreator = projectCreator;
    }

    @PostConstruct
    public void init() {
        //        테스트 생성
        testCreator.creator();
        projectCreator.creator();
    }
}
