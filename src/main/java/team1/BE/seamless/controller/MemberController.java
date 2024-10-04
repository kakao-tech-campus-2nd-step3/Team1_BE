package team1.BE.seamless.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import team1.BE.seamless.DTO.MemberRequestDTO;
import team1.BE.seamless.DTO.MemberResponseDTO;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import team1.BE.seamless.util.page.PageMapper;
import team1.BE.seamless.util.page.PageResult;
import team1.BE.seamless.util.page.SingleResult;


@Tag(name = "팀원 관리")
@RequestMapping("/api/project/{project_id}/member")
@RestController
public class MemberController {

    MemberService memberService;

    @Autowired
    MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "팀원 개별 조회")
    @GetMapping("/{member_id}")
    public SingleResult<MemberEntity> getMember(@PathVariable("member_id") Long memberId) {
        return new SingleResult<>(memberService.getMember(memberId));
    }

    @Operation(summary = "팀원 전체 조회")
    @GetMapping
    public PageResult<MemberEntity> getMemberList(@Valid MemberRequestDTO.getMemberList memberListRequestDTO) {
        return PageMapper.toPageResult(memberService.getMemberList(memberListRequestDTO));
    }


    @Operation(summary = "새 팀원 추가")
    @PostMapping
    public SingleResult<MemberResponseDTO> createMember(
            @PathVariable("project_id") Long projectId,
            @Valid @RequestBody MemberRequestDTO memberRequestDTO) {

        MemberResponseDTO newMember = memberService.createMember(memberRequestDTO, projectId);
        return new SingleResult<>(newMember);
    }

    @Operation(summary = "팀원 정보 수정")
    @PutMapping("/{member_id}")
    public SingleResult<MemberResponseDTO> updateMember(
            @PathVariable("member_id") Long memberId
            , @RequestBody MemberRequestDTO memberRequestDTO) {
        MemberResponseDTO updatedMember = memberService.updateMember(memberId, memberRequestDTO);
        return new SingleResult<>(updatedMember);
    }

        @Operation(summary = "팀원 삭제")
    @DeleteMapping("/{member_id}")
    public SingleResult<MemberResponseDTO> deleteMember(@PathVariable("member_id") Long memberId) {
        MemberResponseDTO memberResponseDTO = memberService.deleteMember(memberId);
        return new SingleResult<>(memberResponseDTO);
    }
}
