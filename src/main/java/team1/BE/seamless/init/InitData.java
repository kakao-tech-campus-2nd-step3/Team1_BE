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
    private final UserCreator userCreator;
    private final OptionCreator optionCreator;
    private final MemberCreator memberCreator;

    @Autowired
    public InitData(TestCreator testCreator, ProjectCreator projectCreator, UserCreator userCreator,
                    OptionCreator optionCreator, MemberCreator memberCreator) {
        this.testCreator = testCreator;
        this.projectCreator = projectCreator;
        this.userCreator = userCreator;
        this.optionCreator = optionCreator;
        this.memberCreator = memberCreator;
    }

    @PostConstruct
    public void init() {
        //        테스트 생성
        testCreator.creator();
        userCreator.creator();
        optionCreator.creator();
        projectCreator.creator();
        memberCreator.creator();
    }
}
