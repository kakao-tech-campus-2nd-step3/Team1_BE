package team1.BE.seamless.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Autowired
    AttendURLService attendURLService;

    @Operation(summary = "팀장이 참여링크 생성 버튼을 누르면 링크 생성")
    @PostMapping("/api/project/{project_id}/invite-link")
    public SingleResult<AttendUrlResponseDTO> generateInviteLink(
            @PathVariable("project_id") String projectId,
            @RequestBody Map<String, String> body) {
        String expirationDate = body.get("expirationDate");
        AttendUrlResponseDTO attendUrlResponseDTO =
                new AttendUrlResponseDTO(attendURLService.generateAttendURL(projectId, expirationDate));
        return new SingleResult<>(attendUrlResponseDTO);
    }
}