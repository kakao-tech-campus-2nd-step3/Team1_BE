package team1.BE.seamless.controller;
// 팀원이 초대링크에 해당하는 페이지에서 이름, 이메일을 작성하여
// 요청을 보낼 때의 컨트롤러 계층

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team1.BE.seamless.DTO.InviteRequestDTO;
import team1.BE.seamless.service.InviteCodeByEmailService;
import team1.BE.seamless.util.errorException.BaseHandler;
import team1.BE.seamless.util.page.SingleResult;

@Tag(name = "이메일 전송")
@RestController
@RequestMapping("/api/project")
public class InviteCodeByEmailController {

    @Autowired
    private InviteCodeByEmailService inviteService;

    @Operation(summary = "이메일로 참여코드 전송하기")
    @PostMapping("/invite")
    public SingleResult<String> inviteMemberToProject(@RequestBody InviteRequestDTO inviteRequest) {
        try {
            String message =
                "You have been invited to join the project with ID: " + inviteRequest.getProjectId()
                    + "\nAnd Participation code: 참여코드 들어가야함";
            inviteService.sendProjectInvite(inviteRequest.getEmail(), message, inviteRequest.getProjectId());
            return new SingleResult<>("이메일로 프로젝트 초대코드 전송이 성공적으로 처리되었습니다.");
        } catch (Exception e) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST,"이메일로 프로젝트 초대코드 전송이 실패되었습니다. : " + e.getMessage());
        }
    }
}