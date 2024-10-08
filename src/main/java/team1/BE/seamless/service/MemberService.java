package team1.BE.seamless.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team1.BE.seamless.DTO.MemberRequestDTO.CreateMember;
import team1.BE.seamless.DTO.MemberRequestDTO.UpdateMember;
import team1.BE.seamless.DTO.MemberRequestDTO.getMemberList;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.mapper.MemberMapper;
import team1.BE.seamless.repository.MemberRepository;
import team1.BE.seamless.repository.ProjectRepository;
import team1.BE.seamless.util.errorException.BaseHandler;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final MemberMapper memberMapper;

    private final ProjectRepository projectRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper,
        ProjectRepository projectRepository) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.projectRepository = projectRepository;
    }

    
    public MemberEntity getMember(Long projectId, Long memberId) {
        return memberRepository.findByIdAndProjectEntityIdAndIsDeleteFalse(memberId, projectId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 멤버가 존재하지 않습니다."));
    }

    public Page<MemberEntity> getMemberList(@Valid Long projectId,
        getMemberList memberListRequestDTO) {
        return memberRepository.findAllByProjectEntityIdAndIsDeleteFalse(projectId,
            memberListRequestDTO.toPageable());
    }

    public MemberEntity createMember(Long projectId, CreateMember create) {
        ProjectEntity project = projectRepository.findById(projectId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

        MemberEntity member = memberMapper.toEntity(create, project);
        memberRepository.save(member);

        return member;
    }

    @Transactional
    public MemberEntity updateMember(Long projectId, Long memberId, UpdateMember update) {
        MemberEntity member = memberRepository.findByIdAndProjectEntityIdAndIsDeleteFalse(memberId,
                projectId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 멤버가 존재하지 않습니다."));

        memberMapper.toUpdate(member, update);
        return member;
    }

    @Transactional
    public MemberEntity deleteMember(Long projectId, Long memberId) {
        MemberEntity member = memberRepository.findByIdAndProjectEntityIdAndIsDeleteFalse(memberId,
                projectId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 멤버가 존재하지 않습니다."));

        member.setDelete(true);

        return member;
    }
}
