package team1.be.seamless.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team1.be.seamless.dto.MemberRequestDTO;
import team1.be.seamless.service.MemberService;

@Component
public class MemberCreator {

    private final MemberService memberService;

    @Autowired
    public MemberCreator(MemberService memberService) {
        this.memberService = memberService;
    }

    public void creator() {

        MemberRequestDTO.CreateMember member1 = new MemberRequestDTO.CreateMember("ex1@gmail.com",
            "sv_XKCT5j5Sm0msQw-mEAmstJ5tq9uBh6c8_QLhzKGo=", "순호");
        memberService.createMember(member1);

        MemberRequestDTO.CreateMember member2 = new MemberRequestDTO.CreateMember("ex2@gmail.com",
            "sv_XKCT5j5Sm0msQw-mEAmstJ5tq9uBh6c8_QLhzKGo=", "동혁");
        memberService.createMember(member2);

        MemberRequestDTO.CreateMember member3 = new MemberRequestDTO.CreateMember("ex3@gmail.com",
            "sv_XKCT5j5Sm0msQw-mEAmstJ5tq9uBh6c8_QLhzKGo=", "도헌");
        memberService.createMember(member3);
    }

}
