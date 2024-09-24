package team1.BE.seamless.init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class InitData {

    private final TestCreator testCreator;

    @Autowired
    public InitData(TestCreator testCreator) {
        this.testCreator = testCreator;
    }

    @PostConstruct
    public void init() {
        //        테스트 생성
        testCreator.creator();
    }
}
