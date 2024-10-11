package team1.BE.seamless.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team1.BE.seamless.DTO.MemberRequestDTO;
import team1.BE.seamless.service.MemberService;

@Component
public class MemberCreator {

    private final MemberService memberService;

    @Autowired
    public MemberCreator(MemberService memberService) {
        this.memberService = memberService;
    }

    public void creator() {
        // 테스트용으로 request데이터 생성
        MemberRequestDTO.CreateMember member1 = new MemberRequestDTO.CreateMember("권순호","MEMBER","ex1@gmail.com","exURL1");
        memberService.createMember(1L, member1); // HttpServletRequest는 null 처리가 안돼서 테스트용으로 새로운 create만듦

        MemberRequestDTO.CreateMember member2 = new MemberRequestDTO.CreateMember("김동혁","USER","ex2@gmail.com","exURL2");
        memberService.createMember(1L, member2); // HttpServletRequest는 null 처리가 안돼서 테스트용으로 새로운 create만듦

        MemberRequestDTO.CreateMember member3 = new MemberRequestDTO.CreateMember("김도헌","MEMBER","ex3@gmail.com","exURL3");
        memberService.createMember(1L, member3); // HttpServletRequest는 null 처리가 안돼서 테스트용으로 새로운 create만듦
    }

}
