package team1.be.seamless.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.repository.ProjectRepository;
import team1.be.seamless.util.MailSend;

@Service
public class ProjectDeadlineReminderService {

    private final ProjectRepository projectRepository;
    private final MailSend mailSend;

    @Autowired
    public ProjectDeadlineReminderService(ProjectRepository projectRepository, MailSend mailSend) {
        this.projectRepository = projectRepository;
        this.mailSend = mailSend;
    }

    // 오후 4시에 이메일 전송함
    @Scheduled(cron = "0 21 14 * * ?")
    public void sendDeadlineReminders() {
        List<ProjectEntity> projects = projectRepository.findAllByIsDeletedFalse();

        for (ProjectEntity project : projects) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endDate = project.getEndDate();
            long daysUntilDeadline = ChronoUnit.DAYS.between(now, endDate);

            if (daysUntilDeadline < 0) continue; // 이미 지난 마감일

            // 마감일이 3일, 1일 남았을 때 이메일 전송
            if (daysUntilDeadline == 3 || daysUntilDeadline == 1) {
                if (project.getMemberEntities().isEmpty()) continue; // 멤버 없는 프로젝트 제외
                String subject = "[프로젝트 마감 임박 알림] '" + project.getName() + "' 프로젝트";
                String message = """
                    프로젝트 %s의 마감 기한이 %d일 남았습니다.
                    
                    프로젝트 관련 작업을 마무리해 주세요.
                    
                    감사합니다.""".formatted(project.getName(), daysUntilDeadline);

                project.getMemberEntities().forEach(member -> {
                    try {
                        mailSend.send(member.getEmail(), subject, message);
                    } catch (Exception e) {
                        System.err.printf("이메일 전송 실패: %s (에러: %s)%n", member.getEmail(), e.getMessage());
                    }
                });
            }
        }
    }
}
