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
//        Optional<MemberEntity> memberEntity = memberRepository.findById(memberId);
//        if (memberEntity.isPresent()) {
//            return memberEntity.get();
//        }
//        else {
//            throw new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음");
//        }
        return memberRepository.findByIdAndProjectEntityIdAndIsDeleteFalse(memberId, projectId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 멤버가 존재하지 않습니다."));
    }

    public Page<MemberEntity> getMemberList(@Valid Long projectId,
        getMemberList memberListRequestDTO) {
        return memberRepository.findAllByProjectEntityIdAndIsDeleteFalse(projectId,
            memberListRequestDTO.toPageable());
    }

    public MemberEntity createMember(Long projectId, CreateMember create) {
//        Optional<ProjectEntity> projectEntity = projectRepository.findById(projectId);
//        if(projectEntity.isPresent()) {
//            MemberEntity memberEntity = memberMapper.toMemberEntity(memberRequestDTO);
//            memberEntity.setProject(projectEntity.get()); // 코드 구조상 어쩔 수 없이 setter 사용..(get메서드가 Optional기능이라 이렇게 함)
//            memberRepository.save(memberEntity);
//            // memberRepository.save(memberEntity) 리턴값은 MemberEntity임 JPA 기능임!
//            MemberResponseDTO memberResponseDTO = new MemberResponseDTO("팀원이 추가되었습니다.");
//            return memberResponseDTO;
//        }
//        else {
//            throw new BaseHandler(HttpStatus.NOT_FOUND, "해당하는 프로젝트가 존재하지 않습니다.");
//        }
        ProjectEntity project = projectRepository.findById(projectId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

        MemberEntity member = memberMapper.toEntity(create, project);
        memberRepository.save(member);

        return member;
    }

    @Transactional
    public MemberEntity updateMember(Long projectId, Long memberId, UpdateMember update) {
//        Optional<MemberEntity> existingMemberEntity = memberRepository.findById(memberId);
//        if(existingMemberEntity.isPresent()) {
//            MemberEntity updatedMember = memberMapper.toMemberUpdateEntity(memberRequestDTO,existingMemberEntity);
//            memberRepository.save(updatedMember);
//            MemberResponseDTO memberResponseDTO = new MemberResponseDTO("팀원 정보가 성공적으로 변경되었습니다.");
//            return memberResponseDTO;
//        }
//        else {
//            throw new BaseHandler(HttpStatus.NOT_FOUND,"해당하는 팀원이 존재하지 않습니다.");
//        }
        MemberEntity member = memberRepository.findByIdAndProjectEntityIdAndIsDeleteFalse(memberId,
                projectId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 멤버가 존재하지 않습니다."));

        memberMapper.toUpdate(member, update);
        return member;
    }

    @Transactional
    public MemberEntity deleteMember(Long projectId, Long memberId) {
//        Optional<MemberEntity> existingMemberEntity = memberRepository.findById(memberId);
//        if(existingMemberEntity.isPresent()) {
//            String name = existingMemberEntity.get().getName();
//            memberRepository.delete(existingMemberEntity.get());
//            return new MemberResponseDTO("팀원 (" + name + ")이 팀에서 삭제되었습니다.");
//        }
//        else {
//            throw new BaseHandler(HttpStatus.NOT_FOUND,"해당하는 팀원이 존재하지 않습니다.");
//        }
        MemberEntity member = memberRepository.findByIdAndProjectEntityIdAndIsDeleteFalse(memberId,
                projectId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 멤버가 존재하지 않습니다."));

        member.setDelete(true);

        return member;
    }
}
