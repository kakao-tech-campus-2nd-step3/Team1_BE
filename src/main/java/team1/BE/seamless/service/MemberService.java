package team1.BE.seamless.service;

import org.springframework.transaction.annotation.Transactional;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberEntity addMember(MemberEntity member, Long projectId) {
//        member.setProject(new ProjectEntity(projectId));
//        return memberRepository.save(member);
        return null;
    }

    @Transactional
    public MemberEntity updateMember(Long id, MemberEntity memberDetails) {
        MemberEntity member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
        member.setEmail(memberDetails.getEmail());
//        member.setJoinNumber(memberDetails.getJoinNumber());
        return memberRepository.save(member);
    }
}
