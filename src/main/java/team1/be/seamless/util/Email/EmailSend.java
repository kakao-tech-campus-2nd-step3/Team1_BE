package team1.be.seamless.util.Email;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.repository.ProjectRepository;
import team1.be.seamless.util.auth.AesEncrypt;
import team1.be.seamless.util.errorException.BaseHandler;

@Service
public class EmailSend {

    private JavaMailSender mailSender;
    private ProjectRepository projectRepository;
    private final AesEncrypt aesEncrypt;

    @Autowired
    EmailSend(JavaMailSender mailSender, ProjectRepository projectRepository,
              AesEncrypt aesEncrypt) {
        this.mailSender = mailSender;
        this.projectRepository = projectRepository;
        this.aesEncrypt = aesEncrypt;
    }

    public void sendProjectInvite(String email, Long projectId, String message, String subject) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

        if (project.getEndDate().isBefore(LocalDateTime.now())) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
        }

        String participationCode = generateParticipationCode(project);

        message += "" + participationCode;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }

    private String generateParticipationCode(ProjectEntity project) {
        String code = aesEncrypt.encrypt(
                project.getId() + "_" + project.getEndDate().withNano(0));
        return code;
    }
}