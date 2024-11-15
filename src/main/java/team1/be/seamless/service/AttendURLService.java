package team1.be.seamless.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.entity.enums.Role;
import team1.be.seamless.repository.ProjectRepository;
import team1.be.seamless.util.auth.AesEncrypt;
import team1.be.seamless.util.errorException.BaseHandler;

@Service
public class AttendURLService {

    private final ProjectRepository projectRepository;
    private final AesEncrypt aesEncrypt;

    @Autowired
    public AttendURLService(ProjectRepository projectRepository, AesEncrypt aesEncrypt) {
        this.projectRepository = projectRepository;
        this.aesEncrypt = aesEncrypt;
    }

    public String generateAttendURL(String email, String role, Long projectId) {
        ProjectEntity project = projectRepository.findByIdAndUserEntityEmailAndIsDeletedFalse(
                        projectId, email)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));

        if (project.isExpired()) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
        }

        if (Role.MEMBER.isRole(role)) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED, "생성 권한이 없습니다.");
        }

        String attendURL = aesEncrypt.encrypt(
                project.getId() + "_" + LocalDateTime.now().plusDays(1).withNano(0));
        return attendURL;
    }

}
