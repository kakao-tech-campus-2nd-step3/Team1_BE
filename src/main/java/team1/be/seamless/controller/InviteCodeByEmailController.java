package team1.be.seamless.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team1.be.seamless.dto.InviteRequestDTO;
import team1.be.seamless.service.InviteCodeByEmailService;
import team1.be.seamless.util.errorException.BaseHandler;
import team1.be.seamless.util.page.SingleResult;

@Tag(name = "이메일 전송")
@RestController
@RequestMapping("/api/project")
public class InviteCodeByEmailController {

    @Autowired
    private InviteCodeByEmailService inviteService;

    @Profile("test")
    @Operation(summary = "이메일로 참여코드 전송하기")
    @PostMapping("/invite")
    public SingleResult<String> inviteMemberToProject(@RequestBody InviteRequestDTO inviteRequest) {
        try {
            inviteService.sendProjectInvite(inviteRequest.getEmail(), inviteRequest.getProjectId(),
                    inviteRequest.getName());
            return new SingleResult<>("팀원의 이메일로 프로젝트 초대코드 전송이 성공적으로 처리되었습니다.");
        } catch (Exception e) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST,
                    "이메일로 프로젝트 초대코드 전송이 실패되었습니다. : " + e.getMessage());
        }
    }
}