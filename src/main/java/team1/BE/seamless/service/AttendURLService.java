package team1.BE.seamless.service;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AttendURLService {

    public String generateAttendURL() {
        String generatedUrl = "https://example.com/" + UUID.randomUUID().toString();
        return generatedUrl;
    }

}
