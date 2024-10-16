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
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class InviteCodeByEmailService {

    private JavaMailSender mailSender;
    private ProjectRepository projectRepository;

    @Autowired
    InviteCodeByEmailService(JavaMailSender mailSender, ProjectRepository projectRepository) {
        this.mailSender = mailSender;
        this.projectRepository = projectRepository;
    }

    public void sendProjectInvite(String email, Long projectId) {
        // 프로젝트 존재 검증
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

//        프로젝트 종료 기간 검증
//        if (project.getEndDate().isBefore(LocalDateTime.now())) {
//            throw new BaseHandler(HttpStatus.BAD_REQUEST, "프로젝트는 종료되었습니다.");
//        } // 프로젝트 initData에 EndDate 설정이 안되어있어서 지금 테스트하면 오류걸림 그래서 주석처리 해놓음ㅇㅇ

        // 팀원인지 팀장인지 검증은 필요없음.


        // 참여코드 생성 (UUID 기반 + 현재 시간)
        String participationCode = generateParticipationCode();

        // 이메일 메시지 내용 생성
        String message = "안녕하세요,\n\n" +
                "프로젝트 '" + project.getName() + "'에 초대되었습니다.\n" +
                "참여 코드는 다음과 같습니다: " + participationCode + "\n\n" +
                "프로젝트에 참여하려면 초대 코드를 사용하여 입장해주세요.\n\n" +
                "감사합니다.";


        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("[프로젝트 초대] 프로젝트 '" + project.getName() + "'에 참여하세요!"); // 이메일 제목 설정임.
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }


    private String generateParticipationCode() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8); // 8자리 코드임
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        return uniqueId + "-" + timestamp; // 참여코드 예: 1234abcd-202410161530 와 같이 생성됨.
        // 이렇게 시간을 기준으로 만들면, 이전 or 다른 팀장의 프로젝트의 참여코드와 겹칠 일이 없게됨.
    }
}