package team1.BE.seamless.service;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AttendURLService {

    public String generateAttendURL(String projectId, String expirationDate) {
        String generatedUrl = "https://seamless.com/invite?code=" + UUID.randomUUID().toString() + "&project_id=" + projectId + "&expires=" + expirationDate;
        return generatedUrl;
    }
}
