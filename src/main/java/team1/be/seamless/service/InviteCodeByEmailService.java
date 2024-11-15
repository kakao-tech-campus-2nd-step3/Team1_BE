package team1.be.seamless.service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.repository.ProjectRepository;
import team1.be.seamless.util.errorException.BaseHandler;

@Service
public class InviteCodeByEmailService {

    private JavaMailSender mailSender;
    private ProjectRepository projectRepository;

    @Autowired
    InviteCodeByEmailService(JavaMailSender mailSender, ProjectRepository projectRepository) {
        this.mailSender = mailSender;
        this.projectRepository = projectRepository;
    }

    public void sendProjectInvite(String email, Long projectId, String name) {

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));


        if (project.isExpired()) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
        }


        String participationCode = generateParticipationCode();

        String message = "안녕하세요,\n\n" + name + "님. " +
                "프로젝트 '" + project.getName() + "'에 초대되었습니다.\n" +
                "참여 코드는 다음과 같습니다: " + participationCode + "\n\n" +
                "프로젝트에 참여하려면 초대 코드를 사용하여 입장해주세요.\n\n" +
                "감사합니다.";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("[프로젝트 초대] 프로젝트 '" + project.getName() + "'에 참여하세요!");
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }


    private String generateParticipationCode() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        return uniqueId + "-" + timestamp;
    }
}