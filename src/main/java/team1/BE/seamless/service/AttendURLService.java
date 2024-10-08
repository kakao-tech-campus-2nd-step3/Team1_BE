package team1.BE.seamless.service;

import static team1.BE.seamless.util.URL.DEFAULTURL;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.repository.ProjectRepository;
import team1.BE.seamless.util.auth.AesEncrypt;
import team1.BE.seamless.util.auth.ParsingPram;
import team1.BE.seamless.util.errorException.BaseHandler;

@Service
public class AttendURLService {

    private final ProjectRepository projectRepository;
    private final ParsingPram parsingPram;
    private final AesEncrypt aesEncrypt;

    @Autowired
    public AttendURLService(ProjectRepository projectRepository, ParsingPram parsingPram,
        AesEncrypt aesEncrypt) {
        this.projectRepository = projectRepository;
        this.parsingPram = parsingPram;
        this.aesEncrypt = aesEncrypt;
    }

    public String generateAttendURL(HttpServletRequest req, @Valid Long projectId) {
        ProjectEntity project = projectRepository.findByIdAndUserEntityEmailAndIsDeletedFalse(projectId,
                parsingPram.getEmail(req))
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));

//        코드는 프로젝트id + exp로 구성
//        exp는 1일로 가정
        String code = aesEncrypt.encrypt(
            project.getId() + " " + LocalDateTime.now().plusDays(1).toString());
        return DEFAULTURL + "invite?code=" + code;
    }

//    public String generateAttendURL(String projectId, String expirationDate) {
//        String generatedUrl = "https://seamless.com/invite?code=" + UUID.randomUUID().toString() + "&project_id=" + projectId + "&expires=" + expirationDate;
//        return generatedUrl;
//    }
}
