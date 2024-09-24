package team1.BE.seamless.controller;

import team1.BE.seamless.DTO.AttendUrlResponseDTO;
import team1.BE.seamless.service.AttendURLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendUrlController {

    @Autowired
    AttendURLService attendURLService;

    @PostMapping("/generate-url")
    public ResponseEntity<AttendUrlResponseDTO> generateUrl() {

        AttendUrlResponseDTO attendUrlResponseDTO = new AttendUrlResponseDTO();
        attendUrlResponseDTO.setAttendUrl(attendURLService.generateAttendURL());
        return ResponseEntity.ok(attendUrlResponseDTO);

    }
}
