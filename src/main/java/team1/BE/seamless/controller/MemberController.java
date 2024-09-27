package team1.BE.seamless.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import team1.BE.seamless.DTO.MemberResponseDTO;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team1.BE.seamless.util.page.SingleResult;

@Tag(name = "팀원 관리")
@RequestMapping("/api/project/{project_id}/member")
@RestController
public class MemberController {
    @Autowired
    MemberService memberService;

//    @Operation(summary = "팀원 삭제")
//    @DeleteMapping("/{member_id}")
//    public SingleResult<MemberEntity> deleteMember(@PathVariable("project_id") Long projectId, @PathVariable("member_id") Long memberId) {
//        memberService.deleteMember(memberId);
//        return new SingleResult<>(new MemberResponseDTO("팀원(" + memberId + ")이 삭제되었습니다."));
//    }

    @Operation(summary = "새 팀원 추가")
    @PostMapping
    public SingleResult<MemberResponseDTO> addMember(@PathVariable("project_id") Long projectId, @RequestBody MemberEntity member) {
        MemberEntity newMember = memberService.addMember(member, projectId);
        return new SingleResult<>(new MemberResponseDTO("새 팀원이 추가되었습니다."));
    }

    @Operation(summary = "팀원 정보 수정")
    @PutMapping("/{member_id}")
    public SingleResult<MemberResponseDTO> updateMember(@PathVariable("project_id") Long projectId, @PathVariable("member_id") Long memberId, @RequestBody MemberEntity memberDetails) {
        MemberEntity updatedMember = memberService.updateMember(memberId, memberDetails);
        return new SingleResult<>(new MemberResponseDTO("팀원 정보가 업데이트 되었습니다."));
    }
}
