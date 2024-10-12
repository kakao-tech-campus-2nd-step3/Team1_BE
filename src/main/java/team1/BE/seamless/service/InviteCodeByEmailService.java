package team1.BE.seamless.service;
// 팀원이 초대링크에 해당하는 페이지에서 이름, 이메일을 작성하여
// 요청을 보낼 때의 서비스 계층

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class InviteCodeByEmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendProjectInvite(String email, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Project participation code");
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}
