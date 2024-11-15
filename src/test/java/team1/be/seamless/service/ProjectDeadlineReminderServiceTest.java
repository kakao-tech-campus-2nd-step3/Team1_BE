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

    @Test
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

        verify(mailSend, never()).send(anyString(), anyString(), anyString());
    }

    @Test
    void 삭제된_프로젝트_제외_테스트() {

        when(projectRepository.findAllByIsDeletedFalse()).thenReturn(Collections.emptyList());

        reminderService.sendDeadlineReminders();

        verify(mailSend, never()).send(anyString(), anyString(), anyString());
    }

    @Test
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
