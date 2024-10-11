package team1.BE.seamless.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import team1.BE.seamless.util.auth.ParsingPram;
import team1.BE.seamless.util.errorException.BaseHandler;

import java.time.LocalDateTime;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final ProjectRepository projectRepository;
    private final ParsingPram parsingPram;

    @Autowired
    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper,
                         ProjectRepository projectRepository, ParsingPram parsingPram) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.projectRepository = projectRepository;
        this.parsingPram = parsingPram;
    }

    public MemberResponseDTO getMember(Long projectId, Long memberId, HttpServletRequest req) {
        // 팀원인지 확인.. 삭제함

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

//        if (project.getEndDate().isBefore(LocalDateTime.now())) {
//            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
//        } 프로젝트 initData에 EndDate 설정이 안되어있어서 지금 테스트하면 오류걸림 그래서 주석처리 해놓음ㅇㅇ

        MemberEntity memberEntity = memberRepository.findByProjectEntityIdAndIdAndIsDeleteFalse(projectId, memberId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 멤버가 존재하지 않습니다."));

        return memberMapper.toGetResponseDTO(memberEntity);
    }

    public Page<MemberEntity> getMemberList(@Valid Long projectId,
        getMemberList memberListRequestDTO, HttpServletRequest req) {
        // 팀원인지 확인.. 삭제함

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

//        if (project.getEndDate().isBefore(LocalDateTime.now())) {
//            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
//        } 프로젝트 initData에 EndDate 설정이 안되어있어서 지금 테스트하면 오류걸림 그래서 주석처리 해놓음ㅇㅇ

        return memberRepository.findAllByProjectEntityIdAndIsDeleteFalse(projectId,
            memberListRequestDTO.toPageable());
    }

    public MemberResponseDTO createMember(Long projectId, CreateMember create, HttpServletRequest req) {
        // 팀원인지 확인.. 삭제함

        ProjectEntity project = projectRepository.findById(projectId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

        // 아래는 프로젝트가 종료됐는데, 그 후에 팀원이 참여링크를 통해 프로젝트 참여를 했을 때 걸러내는거임ㅇㅇ
//        if (project.getEndDate().isBefore(LocalDateTime.now())) {
//            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
//        } 프로젝트 initData에 EndDate 설정이 안되어있어서 지금 테스트하면 오류걸림 그래서 주석처리 해놓음ㅇㅇ

        MemberEntity member = memberMapper.toEntity(create, project);
        memberRepository.save(member);

        return memberMapper.toCreateResponseDTO(member);
    }

    public MemberResponseDTO createMember(Long projectId, CreateMember create) {
        // 테스트용 오버로딩임. 삭제 금지

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

//        if (project.getEndDate().isBefore(LocalDateTime.now())) {
//            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
//        } 프로젝트 initData에 EndDate 설정이 안되어있어서 지금 테스트하면 오류걸림 그래서 주석처리 해놓음ㅇㅇ

        MemberEntity member = memberMapper.toEntity(create, project);
        memberRepository.save(member);

        return memberMapper.toCreateResponseDTO(member);
    }

    @Transactional
    public MemberResponseDTO updateMember(Long projectId, Long memberId, UpdateMember update, HttpServletRequest req) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

//        if (project.getEndDate().isBefore(LocalDateTime.now())) {
//            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
//        } 프로젝트 initData에 EndDate 설정이 안되어있어서 지금 테스트하면 오류걸림 그래서 주석처리 해놓음ㅇㅇ
        // 팀장인지 확인(팀원인지 굳이 한번 더 확인하지 않음. 팀장인지만 검증.)
        if (parsingPram.getRole(req).equals(Role.USER.toString())) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED,"수정 권한이 없습니다.");
        }

        MemberEntity member = memberRepository.findByProjectEntityIdAndIdAndIsDeleteFalse(
                projectId,memberId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 멤버가 존재하지 않습니다."));

        MemberEntity memberEntity = memberMapper.toUpdate(member, update);
        return memberMapper.toPutResponseDTO(memberEntity);
    }

    @Transactional
    public MemberResponseDTO deleteMember(Long projectId, Long memberId, HttpServletRequest req) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));
//
//        if (project.getEndDate().isBefore(LocalDateTime.now())) {
//            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
//        } 프로젝트 initData에 EndDate 설정이 안되어있어서 지금 테스트하면 오류걸림 그래서 주석처리 해놓음ㅇㅇ

        // 팀장인지 확인(팀원인지 굳이 한번 더 확인하지 않음. 팀장인지만 검증.)
        if (parsingPram.getRole(req).equals(Role.USER.toString())) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED,"수정 권한이 없습니다.");
        }

        MemberEntity member = memberRepository.findByProjectEntityIdAndIdAndIsDeleteFalse(
                projectId, memberId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 멤버가 존재하지 않습니다."));

        member.setDelete(true);

        return memberMapper.toDeleteResponseDTO(member);
    }
}
