package team1.be.seamless.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team1.be.seamless.dto.MemberRequestDTO;
import team1.be.seamless.dto.MemberResponseDTO;
import team1.be.seamless.service.MemberService;
import team1.be.seamless.util.auth.ParsingParam;
import team1.be.seamless.util.page.PageMapper;
import team1.be.seamless.util.page.PageResult;
import team1.be.seamless.util.page.SingleResult;

@Tag(name = "팀원 관리")
@RequestMapping("/api/project/{projectId}/member")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final ParsingParam parsingParam;

    @Autowired
    public MemberController(MemberService memberService, ParsingParam parsingParam) {
        this.memberService = memberService;
        this.parsingParam = parsingParam;
    }

    @Operation(summary = "팀원 개별 조회")
    @GetMapping("/{memberId}")
    public SingleResult<MemberResponseDTO> getMember(
        @Valid @PathVariable("projectId") Long projectId,
        @Valid @PathVariable("memberId") Long memberId,
        HttpServletRequest req) {
        return new SingleResult<>(memberService.getMember(projectId, memberId, parsingParam.getRole(req)));
    }

    @Operation(summary = "팀원 전체 조회")
    @GetMapping
    public PageResult<MemberResponseDTO> getMemberList(
        @Valid @PathVariable("projectId") Long projectId,
        HttpServletRequest req,
        @Valid MemberRequestDTO.getMemberList memberListRequestDTO) {
        return PageMapper.toPageResult(
            memberService.getMemberList(projectId, memberListRequestDTO, parsingParam.getRole(req)));
    }


    @Operation(summary = "새 팀원 추가")
    @PostMapping
    public SingleResult<MemberResponseDTO> createMember(
        @Valid @RequestBody MemberRequestDTO.CreateMember Create) {
        return new SingleResult<>(memberService.createMember(Create));
    }

    @Operation(summary = "팀원 정보 수정")
    @PutMapping("/{memberId}")
    public SingleResult<MemberResponseDTO> updateMember(
        @PathVariable("projectId") Long projectId,
        @PathVariable("memberId") Long memberId,
        @RequestBody MemberRequestDTO.UpdateMember update,
        HttpServletRequest req) {
        return new SingleResult<>(memberService.updateMember(projectId, memberId, update, parsingParam.getRole(req)));
    }

    @Operation(summary = "팀원 삭제")
    @DeleteMapping("/{memberId}")
    public SingleResult<MemberResponseDTO> deleteMember(
        @PathVariable("projectId") Long projectId,
        @PathVariable("memberId") Long memberId,
        HttpServletRequest req) {
        return new SingleResult<>(memberService.deleteMember(projectId, memberId, parsingParam.getRole(req)));
    }
}
