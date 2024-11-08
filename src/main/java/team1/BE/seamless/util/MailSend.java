package team1.BE.seamless.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailSend {
    private JavaMailSender mailSender;

    @Autowired
    public MailSend(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void send(String to, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject); // 이메일 제목 설정임.
        mailMessage.setText(text);
        mailSender.send(mailMessage);
    }
}
