package team1.be.seamless.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team1.be.seamless.entity.MemberEntity;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.repository.ProjectRepository;
import team1.be.seamless.util.MailSend;

class ProjectDeadlineReminderServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private MailSend mailSend;

    @InjectMocks
    private ProjectDeadlineReminderService reminderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test // 마감 기한이 3일 또는 1일 남은 경우 이메일 전송
    void 마감_기한_임박_알림_테스트() {

        LocalDateTime now = LocalDateTime.now();

        ProjectEntity project1 = new ProjectEntity();
        project1.setId(1L);
        project1.setName("Project A");
        project1.setEndDate(now.plusDays(3));

        ProjectEntity project2 = new ProjectEntity();
        project2.setId(2L);
        project2.setName("Project B");
        project2.setEndDate(now.plusDays(1));

        MemberEntity member1 = new MemberEntity();
        member1.setEmail("member1@example.com");

        MemberEntity member2 = new MemberEntity();
        member2.setEmail("member2@example.com");

        project1.setMemberEntities(List.of(member1));
        project2.setMemberEntities(List.of(member2));

        when(projectRepository.findAllByIsDeletedFalse()).thenReturn(List.of(project1, project2));

        reminderService.sendDeadlineReminders();

        verify(mailSend, times(1)).send(eq("member1@example.com"), contains("[프로젝트 마감 임박 알림]"), contains("3일"));
        verify(mailSend, times(1)).send(eq("member2@example.com"), contains("[프로젝트 마감 임박 알림]"), contains("1일"));
    }

    @Test // 마감 기한이 임박하지 않은 경우 이메일 전송 안 함
    void 마감_기한_임박하지_않음_테스트() {

        LocalDateTime now = LocalDateTime.now();

        ProjectEntity project = new ProjectEntity();
        project.setId(1L);
        project.setName("Project C");
        project.setEndDate(now.plusDays(5));

        MemberEntity member = new MemberEntity();
        member.setEmail("member@example.com");

        project.setMemberEntities(List.of(member));

        when(projectRepository.findAllByIsDeletedFalse()).thenReturn(List.of(project));

        reminderService.sendDeadlineReminders();

        verify(mailSend, never()).send(anyString(), anyString(), anyString()); // 이메일 전송이 호출되지 않는거임ㅇㅇ
    }

    @Test // 삭제된 프로젝트가 제외되는지 확인
    void 삭제된_프로젝트_제외_테스트() {

        when(projectRepository.findAllByIsDeletedFalse()).thenReturn(Collections.emptyList());

        reminderService.sendDeadlineReminders();

        verify(mailSend, never()).send(anyString(), anyString(), anyString());
    }

    @Test // 프로젝트에 멤버가 없는 경우 이메일 전송 안 함
    void 프로젝트_멤버_없음_테스트() {

        LocalDateTime now = LocalDateTime.now();

        ProjectEntity project = new ProjectEntity();
        project.setId(1L);
        project.setName("Project D");
        project.setEndDate(now.plusDays(3));
        project.setMemberEntities(Collections.emptyList());

        when(projectRepository.findAllByIsDeletedFalse()).thenReturn(List.of(project));

        reminderService.sendDeadlineReminders();

        verify(mailSend, never()).send(anyString(), anyString(), anyString());
    }
}
