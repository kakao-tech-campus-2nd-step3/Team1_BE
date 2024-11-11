package team1.BE.seamless.service;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team1.BE.seamless.DTO.MemberRequestDTO;
import team1.BE.seamless.DTO.MemberRequestDTO.UpdateMember;
import team1.BE.seamless.DTO.MemberRequestDTO.getMemberList;
import team1.BE.seamless.DTO.MemberResponseDTO;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.enums.Role;
import team1.BE.seamless.mapper.MemberMapper;
import team1.BE.seamless.repository.MemberRepository;
import team1.BE.seamless.repository.ProjectRepository;
import team1.BE.seamless.util.MailSend;
import team1.BE.seamless.util.Util;
import team1.BE.seamless.util.auth.AesEncrypt;
import team1.BE.seamless.util.auth.ParsingPram;
import team1.BE.seamless.util.errorException.BaseHandler;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final ProjectRepository projectRepository;
    private final ParsingPram parsingPram;
    private final AesEncrypt aesEncrypt;
//    private final EmailSend emailSend;
    private final MailSend mailSend;

//    @Autowired
//    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper,
//        ProjectRepository projectRepository, ParsingPram parsingPram, AesEncrypt aesEncrypt, EmailSend emailSend) {
//        this.memberRepository = memberRepository;
//        this.memberMapper = memberMapper;
//        this.projectRepository = projectRepository;
//        this.parsingPram = parsingPram;
//        this.aesEncrypt = aesEncrypt;
//        this.emailSend = emailSend;
//
//    }


    @Autowired
    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper,
        ProjectRepository projectRepository, ParsingPram parsingPram, AesEncrypt aesEncrypt,
        MailSend mailSend) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.projectRepository = projectRepository;
        this.parsingPram = parsingPram;
        this.aesEncrypt = aesEncrypt;
        this.mailSend = mailSend;
    }

    public MemberResponseDTO getMember(Long projectId, Long memberId, HttpServletRequest req) {
        // 팀원인지 확인하기
        if (parsingPram.getRole(req).equals(Role.MEMBER.toString())) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED,"권한이 없습니다.");
        }

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));


        MemberEntity memberEntity = memberRepository.findByProjectEntityIdAndIdAndIsDeleteFalse(projectId, memberId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 멤버가 존재하지 않습니다."));

        if (memberEntity.getProjectEntity().getEndDate().isBefore(LocalDateTime.now())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
        }

        return memberMapper.toGetResponseDTO(memberEntity);
    }

    public Page<MemberResponseDTO> getMemberList(Long projectId,
        getMemberList memberList, HttpServletRequest req) {
        // 팀원인지 확인하기
        if (parsingPram.getRole(req).equals(Role.MEMBER.toString())) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED,"권한이 없습니다.");
        }

         return memberRepository.findAllByProjectEntityIdAndIsDeleteFalse(projectId,memberList.toPageable()).map(memberMapper::toGetResponseDTO);

    }


    @Transactional
    public MemberResponseDTO createMember(MemberRequestDTO.CreateMember create) {

//        프로젝트id, exp
        Long projectId = Long.parseLong(aesEncrypt.decrypt(create.getAttendURL()).split("_")[0]);
        LocalDateTime exp = Util.parseDate(aesEncrypt.decrypt(create.getAttendURL()).split("_")[1]);

//        exp검사
        if (exp.isBefore(LocalDateTime.now())) {
            throw new BaseHandler(HttpStatus.FORBIDDEN,"초대 코드가 만료되었습니다.");
        }

//       멤버 이메일 중복 여부 검사
//        if(memberRepository.findByEmailAndIsDeleteFalse(create.getEmail()).isPresent()){
//            throw new BaseHandler(HttpStatus.UNAUTHORIZED,"이메일이 중복 됩니다.");
//        }

//        프로젝트 조회
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

        if (project.getEndDate().isBefore(LocalDateTime.now())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
        }

//       멤버 이메일 중복 여부 검사
        for (MemberEntity member : project.getMemberEntities()){
            if (member.getEmail().equals(create.getEmail()) && Boolean.FALSE.equals(member.getIsDelete())) {
                throw new BaseHandler(HttpStatus.UNAUTHORIZED,"이메일이 중복 됩니다.");
            }
        }

        MemberEntity member = memberMapper.toEntity(create, project);
        memberRepository.save(member);

//        코드 생성
        String code = aesEncrypt.encrypt(project.getId().toString());


//      이메일로 코드 전달
//        String email = create.getEmail();
        String subject = "[프로젝트 초대] 프로젝트 '" + project.getName() + "'에 참여하세요!";
        String message = "안녕하세요,\n\n" + create.getName() + "님. " +
                "프로젝트 '" + project.getName() + "'에 초대되었습니다.\n" + "\n\n" +
                "프로젝트에 참여하려면 초대 코드를 사용하여 입장해주세요.\n\n" +
                "감사합니다.\n\n" + "참여 코드는 다음과 같습니다: \n"  + code;

        mailSend.send(create.getEmail(),subject,message);

        return memberMapper.toCreateResponseDTO(member, code);
    }


    @Transactional
    public MemberResponseDTO updateMember(Long projectId, Long memberId, UpdateMember update, HttpServletRequest req) {
        // 팀장인지 확인(팀원인지 굳이 한번 더 확인하지 않음. 팀장인지만 검증.
        if (parsingPram.getRole(req).equals(Role.USER.toString())) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED,"수정 권한이 없습니다.");
        }

//        ProjectEntity project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));
//
//        if (project.getEndDate().isBefore(LocalDateTime.now())) {
//            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
//        }

        MemberEntity member = memberRepository.findByProjectEntityIdAndIdAndIsDeleteFalse(
                projectId,memberId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 멤버가 존재하지 않습니다."));

        if (member.getProjectEntity().getEndDate().isBefore(LocalDateTime.now())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
        }

        MemberEntity memberEntity = memberMapper.toUpdate(member, update);
        return memberMapper.toPutResponseDTO(memberEntity);
    }

    @Transactional
    public MemberResponseDTO deleteMember(Long projectId, Long memberId, HttpServletRequest req) {
        // 팀장인지 확인(팀원인지 굳이 한번 더 확인하지 않음. 팀장인지만 검증.)
        if (parsingPram.getRole(req).equals(Role.USER.toString())) {
            throw new BaseHandler(HttpStatus.FORBIDDEN,"삭제 권한이 없습니다.");
        }

//        ProjectEntity project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));
//
//        if (project.getEndDate().isBefore(LocalDateTime.now())) {
//            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
//        }

        MemberEntity member = memberRepository.findByProjectEntityIdAndIdAndIsDeleteFalse(
                projectId, memberId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 멤버가 존재하지 않습니다."));

        if (member.getProjectEntity().getEndDate().isBefore(LocalDateTime.now())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
        }

        member.setDelete(true);

        return memberMapper.toDeleteResponseDTO(member);
    }
}
