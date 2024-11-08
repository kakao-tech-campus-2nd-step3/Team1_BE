package team1.BE.seamless.util.Email;
// 팀원이 초대링크에 해당하는 페이지에서 이름, 이메일을 작성하여
// 요청을 보낼 때의 서비스 계층

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.repository.ProjectRepository;
import team1.BE.seamless.util.auth.AesEncrypt;
import team1.BE.seamless.util.errorException.BaseHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class EmailSend {

    private JavaMailSender mailSender;
    private ProjectRepository projectRepository;
    private final AesEncrypt aesEncrypt;

    @Autowired
    EmailSend(JavaMailSender mailSender, ProjectRepository projectRepository, AesEncrypt aesEncrypt) {
        this.mailSender = mailSender;
        this.projectRepository = projectRepository;
        this.aesEncrypt = aesEncrypt;
    }

    public void sendProjectInvite(String email, Long projectId, String message, String subject) {
        // 프로젝트 존재 검증
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

        // 프로젝트 종료 기간 검증
        if (project.getEndDate().isBefore(LocalDateTime.now())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
        }

        // 팀원인지 팀장인지 검증은 필요없음.(어차피 이 post요청은 아무 권한 없는 사람이 보내는 것이기 때문임)


        // 참여코드 생성 (UUID 기반 + 현재 시간)
        String participationCode = generateParticipationCode(project);

        // 이메일 메시지 내용 생성
        message += "" + participationCode;


        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject); // 이메일 제목 설정임.
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }

    // 참여 링크랑 너무 똑같을 것 같아서 EndDate를 기반으로 참여 코드 만듦.
    private String generateParticipationCode(ProjectEntity project) {
        String code = aesEncrypt.encrypt(
                project.getId() + "_" + project.getEndDate().withNano(0));
        return code;
    }
}