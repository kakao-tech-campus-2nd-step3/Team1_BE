package team1.BE.seamless.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team1.BE.seamless.DTO.MemberRequestDTO;
import team1.BE.seamless.DTO.MemberRequestDTO.CreateMember;
import team1.BE.seamless.DTO.MemberRequestDTO.UpdateMember;
import team1.BE.seamless.DTO.MemberRequestDTO.getMemberList;
import team1.BE.seamless.DTO.MemberResponseDTO;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.enums.Role;
import team1.BE.seamless.mapper.MemberMapper;
import team1.BE.seamless.repository.MemberRepository;
import team1.BE.seamless.repository.ProjectRepository;
import team1.BE.seamless.util.Util;
import team1.BE.seamless.util.auth.AesEncrypt;
import team1.BE.seamless.util.auth.ParsingPram;
import team1.BE.seamless.util.errorException.BaseHandler;

import java.time.LocalDateTime;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final ProjectRepository projectRepository;
    private final ParsingPram parsingPram;
    private final AesEncrypt aesEncrypt;
    private final InviteCodeByEmailService inviteCodeByEmailService;

    @Autowired
    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper,
        ProjectRepository projectRepository, ParsingPram parsingPram, AesEncrypt aesEncrypt, InviteCodeByEmailService inviteCodeByEmailService) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.projectRepository = projectRepository;
        this.parsingPram = parsingPram;
        this.aesEncrypt = aesEncrypt;
        this.inviteCodeByEmailService = inviteCodeByEmailService;
    }

    public MemberResponseDTO getMember(Long projectId, Long memberId) {
        // 팀원인지 확인할 필요 없음. 팀원이든 팀장이든 다 가능해야하니까!

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

        if (project.getEndDate().isBefore(LocalDateTime.now())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
        }

        MemberEntity memberEntity = memberRepository.findByProjectEntityIdAndIdAndIsDeleteFalse(projectId, memberId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 멤버가 존재하지 않습니다."));

        return memberMapper.toGetResponseDTO(memberEntity);
    }

    public Page<MemberEntity> getMemberList(@Valid Long projectId,
        getMemberList memberListRequestDTO) {
        // 팀원인지 확인할 필요 없음. 팀원이든 팀장이든 다 가능해야하니까!

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

        if (project.getEndDate().isBefore(LocalDateTime.now())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
        }

        return memberRepository.findAllByProjectEntityIdAndIsDeleteFalse(projectId,
            memberListRequestDTO.toPageable());
    }


    // 멤버 초기 데이터 때문에 오버로딩한 메서드임
    @Transactional
    public MemberResponseDTO createMember(MemberRequestDTO.CreateMember create) {

//        프로젝트id, exp
        Long projectId = Long.parseLong(aesEncrypt.decrypt(create.getCode()).split("_")[0]);
        LocalDateTime exp = Util.parseDate(aesEncrypt.decrypt(create.getCode()).split("_")[1]);

//        exp검사
        if (exp.isBefore(LocalDateTime.now())) {
            throw new BaseHandler(HttpStatus.FORBIDDEN,"초대 코드가 만료되었습니다.");
        }

//       멤버 이메일 중복 여부 검사
        if(memberRepository.findByEmailAndIsDeleteFalse(create.getEmail()).isPresent()){
            throw new BaseHandler(HttpStatus.UNAUTHORIZED,"이메일이 중복 됩니다.");
        }

//        프로젝트 조회
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

        if (project.getEndDate().isBefore(LocalDateTime.now())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
        }

        MemberEntity member = memberMapper.toEntity(create, project);
        memberRepository.save(member);

//        코드 생성
        String code = aesEncrypt.encrypt(member.getId().toString());
        System.out.println(code);

//        이메일로 코드 전달(추가 요망)

        return memberMapper.toCreateResponseDTO(member, code);
    }

    @Transactional
    public MemberResponseDTO updateMember(Long projectId, Long memberId, UpdateMember update, HttpServletRequest req) {
        // 팀장인지 확인(팀원인지 굳이 한번 더 확인하지 않음. 팀장인지만 검증.
        if (parsingPram.getRole(req).equals(Role.USER.toString())) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED,"수정 권한이 없습니다.");
        }

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

        if (project.getEndDate().isBefore(LocalDateTime.now())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
        }

        MemberEntity member = memberRepository.findByProjectEntityIdAndIdAndIsDeleteFalse(
                projectId,memberId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 멤버가 존재하지 않습니다."));

        MemberEntity memberEntity = memberMapper.toUpdate(member, update);
        return memberMapper.toPutResponseDTO(memberEntity);
    }

    @Transactional
    public MemberResponseDTO deleteMember(Long projectId, Long memberId, HttpServletRequest req) {
        // 팀장인지 확인(팀원인지 굳이 한번 더 확인하지 않음. 팀장인지만 검증.)
        if (parsingPram.getRole(req).equals(Role.MEMBER.toString())) {
            throw new BaseHandler(HttpStatus.FORBIDDEN,"삭제 권한이 없습니다.");
        }

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

        if (project.getEndDate().isBefore(LocalDateTime.now())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
        }

        MemberEntity member = memberRepository.findByProjectEntityIdAndIdAndIsDeleteFalse(
                projectId, memberId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 멤버가 존재하지 않습니다."));

        member.setDelete(true);

        return memberMapper.toDeleteResponseDTO(member);
    }
}
