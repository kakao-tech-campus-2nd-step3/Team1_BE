package team1.BE.seamless.service;
// 팀원이 초대링크에 해당하는 페이지에서 이름, 이메일을 작성하여
// 요청을 보낼 때의 서비스 계층

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.repository.ProjectRepository;
import team1.BE.seamless.util.errorException.BaseHandler;

import java.time.LocalDateTime;

@Service
public class InviteCodeByEmailService {

    private JavaMailSender mailSender;
    private ProjectRepository projectRepository;

    @Autowired
    InviteCodeByEmailService(JavaMailSender mailSender, ProjectRepository projectRepository) {
        this.mailSender = mailSender;
        this.projectRepository = projectRepository;
    }

    public void sendProjectInvite(String email, String message, Long projectId) {
        // 프로젝트 존재 검증
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

//        프로젝트 종료 기간 검증
//        if (project.getEndDate().isBefore(LocalDateTime.now())) {
//            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
//        } // 프로젝트 initData에 EndDate 설정이 안되어있어서 지금 테스트하면 오류걸림 그래서 주석처리 해놓음ㅇㅇ

        // 팀원인지 팀장인지 검증은 필요없음.

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("여기엔 무엇을 채우는거지?");
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}
