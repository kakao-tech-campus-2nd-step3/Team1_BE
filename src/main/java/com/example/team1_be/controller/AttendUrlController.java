package com.example.team1_be.controller;

import com.example.team1_be.DTO.AttendUrlResponseDTO;
import com.example.team1_be.service.AttendURLService;
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
