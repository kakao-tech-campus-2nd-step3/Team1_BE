package team1.BE.seamless.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.enums.Role;
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

    public String generateAttendURL(HttpServletRequest req, Long projectId) {
        ProjectEntity project = projectRepository.findByIdAndUserEntityEmailAndIsDeletedFalse(projectId,parsingPram.getEmail(req))
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));

        //현재 시간이 프로젝트 종료 기한을 넘어섰는지 체크
        if (project.getEndDate().isBefore(LocalDateTime.now())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
        }

        // 팀장인지 확인(팀원인지 굳이 한번 더 확인하지 않음. 팀장인지만 검증.)
        if (parsingPram.getRole(req).equals(Role.MEMBER.toString())) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED,"생성 권한이 없습니다.");
        }


//        참여 링크를 URL 형식으로 내려줄 필요가 없음. code랑 비슷한 형식으로 내려주면 프론트쪽에서 URL 형식으로 바꿔줄거임
//        참여 링크는 프로젝트id + exp로 구성
//        exp는 1일로 가정
        String attendURL = aesEncrypt.encrypt(
                project.getId() + "_" + LocalDateTime.now().plusDays(1).withNano(0));
        return attendURL;
    }

}
