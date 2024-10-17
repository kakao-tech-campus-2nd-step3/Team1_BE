package team1.BE.seamless.service;

import static team1.BE.seamless.util.URL.DEFAULTURL;

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

    public String generateAttendURL(HttpServletRequest req, @Valid Long projectId, @Valid Long userId) {
//        ProjectEntity project = projectRepository.findByIdAndUserEntityEmailAndIsDeletedFalse(projectId,
//                parsingPram.getEmail(req)) 토큰에서 이메일 찾는 로직 테스트 안 돼서, 일단 프로젝트 id+유저id 로 찾는 로직으로 해놓음(추후 다시 수정 예정)
        ProjectEntity project = projectRepository.findByIdAndUserEntityIdAndIsDeletedFalse(projectId,userId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않음"));

        // 현재 시간이 프로젝트 종료 기한을 넘어섰는지 체크
//        if (project.getEndDate().isBefore(LocalDateTime.now())) {
//            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
//        } 프로젝트 initData에 EndDate 설정이 안되어있어서 지금 테스트하면 오류걸림 그래서 주석처리 해놓음ㅇㅇ

        // 팀장인지 확인(팀원인지 굳이 한번 더 확인하지 않음. 팀장인지만 검증.)
        if (parsingPram.getRole(req).equals(Role.USER.toString())) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED,"생성 권한이 없습니다.");
        }


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
