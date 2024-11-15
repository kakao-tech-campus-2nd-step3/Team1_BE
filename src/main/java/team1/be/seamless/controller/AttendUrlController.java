package team1.be.seamless.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import team1.be.seamless.service.AttendURLService;
import team1.be.seamless.util.auth.ParsingParam;
import team1.be.seamless.util.page.SingleResult;

@Tag(name = "참여 링크 생성")
@RestController
public class AttendUrlController {

    private final AttendURLService attendURLService;
    private final ParsingParam parsingParam;

    @Autowired
    public AttendUrlController(AttendURLService attendURLService, ParsingParam parsingParam) {
        this.attendURLService = attendURLService;
        this.parsingParam = parsingParam;
    }

    /**
     * 팉장의 토큰과 프로젝트id로 프로젝트 존재 검증 프로젝트id + " " + exp로 코드 생성 코드를 양방향 암호화 ex)
     */
    @Operation(summary = "팀원초대 코드 생성")
    @PostMapping("/api/project/{projectId}/invite-link")
    public SingleResult<String> generateInviteLink(HttpServletRequest req,
                                                   @Valid @PathVariable("projectId") Long projectId) {
        return new SingleResult<>(attendURLService.generateAttendURL(parsingParam.getEmail(req),
                parsingParam.getRole(req), projectId));
    }
}