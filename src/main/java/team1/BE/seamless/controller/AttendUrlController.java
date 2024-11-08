package team1.BE.seamless.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import team1.BE.seamless.service.AttendURLService;
import team1.BE.seamless.util.page.SingleResult;

@Tag(name = "어텐드 링크 생성")
@RestController
public class AttendUrlController {

    private final AttendURLService attendURLService;

    @Autowired
    public AttendUrlController(AttendURLService attendURLService) {
        this.attendURLService = attendURLService;
    }

    /**
     * 팉장의 토큰과 프로젝트id로 프로젝트 존재 검증 프로젝트id + " " + exp로 코드 생성 코드를 양방향 암호화 ex)
     */
    @Operation(summary = "초대용 어텐드링크 생성")
    @PostMapping("/api/project/{projectId}/invite-link/{userId}")
    public SingleResult<String> generateInviteLink(HttpServletRequest req,
        @Valid @PathVariable("projectId") Long projectId,
        @Valid @PathVariable("userId") Long userId) {
        return new SingleResult<>(attendURLService.generateAttendURL(req, projectId, userId));
    }
}