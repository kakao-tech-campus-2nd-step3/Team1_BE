package team1.BE.seamless.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import team1.BE.seamless.DTO.AttendUrlResponseDTO;
import team1.BE.seamless.service.AttendURLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import team1.BE.seamless.util.page.SingleResult;

import java.util.Map;

@Tag(name = "참여 링크 생성")
@RestController
public class AttendUrlController {

    private final AttendURLService attendURLService;

    @Autowired
    public AttendUrlController(AttendURLService attendURLService) {
        this.attendURLService = attendURLService;
    }

//    @Operation(summary = "팀장이 참여링크 생성 버튼을 누르면 링크 생성")
//    @PostMapping("/api/project/{project_id}/invite-link")
//    public SingleResult<AttendUrlResponseDTO> generateInviteLink(
//            @PathVariable("project_id") String projectId,
//            @RequestBody Map<String, String> body) {
//        String expirationDate = body.get("expirationDate");
//        AttendUrlResponseDTO attendUrlResponseDTO =
//                new AttendUrlResponseDTO(attendURLService.generateAttendURL(projectId, expirationDate));
//        return new SingleResult<>(attendUrlResponseDTO);
//    }

    /**
     * 팉장의 토큰과 프로젝트id로 프로젝트 존재 검증
     * 프로젝트id + " " + exp로 코드 생성
     * 코드를 양방향 암호화
     * ex) http://localhost:8080/api/memverInvite?code="123456"
     * 검증시 코드를 복호화 해서 프로젝트id와 exp를 검증(server less)
     * */
    @Operation(summary = "팀원초대 링크 생성")
    @PostMapping("/api/project/{project_id}/invite-link")
    public SingleResult<String> generateInviteLink(HttpServletRequest req, @Valid  @PathVariable("project_id") Long projectId){
        return new SingleResult<>(attendURLService.generateAttendURL(req, projectId));
    }
}