package team1.be.seamless.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import team1.be.seamless.dto.MemberRequestDTO;
import team1.be.seamless.dto.MemberRequestDTO.UpdateMember;
import team1.be.seamless.dto.MemberResponseDTO;
import team1.be.seamless.entity.MemberEntity;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.entity.enums.Role;
import team1.be.seamless.mapper.MemberMapper;
import team1.be.seamless.repository.MemberRepository;
import team1.be.seamless.repository.ProjectRepository;
import team1.be.seamless.util.MailSend;
import team1.be.seamless.util.auth.AesEncrypt;
import team1.be.seamless.util.errorException.BaseHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private AesEncrypt aesEncrypt;

    @Mock
    private MailSend mailSend;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test // 특정 멤버를 정상적으로 조회할 수 있는지 검증
    void 특정_멤버_정상_조회_테스트() {
        Long projectId = 1L;
        Long memberId = 1L;
        String role = Role.USER.toString();

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setEndDate(LocalDateTime.now().plusDays(10));

        MemberEntity memberEntity = new MemberEntity(projectEntity);

        when(memberRepository.findByProjectEntityIdAndIdAndIsDeleteFalse(projectId, memberId))
                .thenReturn(Optional.of(memberEntity));
        when(memberMapper.toGetResponseDTO(memberEntity)).thenReturn(new MemberResponseDTO());

        MemberResponseDTO result = memberService.getMember(projectId, memberId, role);

        assertNotNull(result);
        verify(memberRepository, times(1)).findByProjectEntityIdAndIdAndIsDeleteFalse(projectId, memberId);
    }

    @Test // 프로젝트의 모든 멤버를 정상적으로 조회할 수 있는지 검증
    void 프로젝트_멤버_전체_조회_테스트() {
        Long projectId = 1L;
        String role = Role.USER.toString();

        MemberRequestDTO.getMemberList memberListDTO = new MemberRequestDTO.getMemberList();

        Page<MemberEntity> page = new PageImpl<>(List.of(new MemberEntity()));
        when(memberRepository.findAllByProjectEntityIdAndIsDeleteFalse(eq(projectId), any()))
                .thenReturn(page);

        Page<MemberResponseDTO> result = memberService.getMemberList(projectId, memberListDTO, role);

        assertNotNull(result);
        verify(memberRepository, times(1)).findAllByProjectEntityIdAndIsDeleteFalse(eq(projectId), any());
    }

    @Test // 새 멤버를 생성하고 데이터베이스에 저장하는 기능이 올바르게 작동하는지 검증
    void 새_멤버_생성_저장_테스트() {
        String attendURL = "sv_XKCT5j5Sm0msQw-mEAmstJ5tq9uBh6c8_QLhzKGo=";
        MemberRequestDTO.CreateMember createMember = new MemberRequestDTO.CreateMember("test@example.com", attendURL, "테스트 이름");

        String decryptedAttendURL = "2_2024-12-31T23:59:59";
        when(aesEncrypt.decrypt(attendURL)).thenReturn(decryptedAttendURL);

        Long projectId = 2L;
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(projectId);
        projectEntity.setName("Test Project");
        projectEntity.setEndDate(LocalDateTime.now().plusDays(10));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectEntity));

        MemberEntity memberEntity = new MemberEntity();
        when(memberMapper.toEntity(createMember, projectEntity)).thenReturn(memberEntity);
        when(memberRepository.save(memberEntity)).thenReturn(memberEntity);

        memberService.createMember(createMember);

        verify(aesEncrypt, times(2)).decrypt(attendURL);
        verify(projectRepository, times(1)).findById(projectId);
        verify(memberMapper, times(1)).toEntity(createMember, projectEntity);
        verify(memberRepository, times(1)).save(memberEntity);
    }

    @Test // 기존 멤버 정보를 수정하는 기능이 정상적으로 작동하는지 검증
    void 멤버_정보_수정_테스트() {
        Long projectId = 1L;
        Long memberId = 1L;
        String role = Role.USER.toString();
        MemberRequestDTO.UpdateMember updateMember = new MemberRequestDTO.UpdateMember("New 이름", "New 역할", "new@example.com", "imageURL");

        ProjectEntity projectEntity = new ProjectEntity();
        MemberEntity memberEntity = new MemberEntity(projectEntity);
        projectEntity.setEndDate(LocalDateTime.now().plusDays(10));

        when(memberRepository.findByProjectEntityIdAndIdAndIsDeleteFalse(projectId, memberId)).thenReturn(Optional.of(memberEntity));
        when(memberMapper.toUpdate(memberEntity, updateMember)).thenReturn(memberEntity);
        when(memberMapper.toPutResponseDTO(memberEntity)).thenReturn(new MemberResponseDTO());

        MemberResponseDTO result = memberService.updateMember(projectId, memberId, updateMember, role);

        assertNotNull(result);
    }

    @Test // 특정 멤버를 삭제하는 기능이 올바르게 작동하는지 검증
    void 멤버_삭제_테스트() {
        Long projectId = 1L;
        Long memberId = 1L;
        String role = Role.USER.toString();

        ProjectEntity projectEntity = new ProjectEntity();
        MemberEntity memberEntity = new MemberEntity(projectEntity);
        projectEntity.setEndDate(LocalDateTime.now().plusDays(10));

        when(memberRepository.findByProjectEntityIdAndIdAndIsDeleteFalse(projectId, memberId)).thenReturn(Optional.of(memberEntity));
        when(memberMapper.toDeleteResponseDTO(memberEntity)).thenReturn(new MemberResponseDTO());

        MemberResponseDTO result = memberService.deleteMember(projectId, memberId, role);

        assertNotNull(result);
        verify(memberRepository, times(1)).findByProjectEntityIdAndIdAndIsDeleteFalse(projectId, memberId);
    }
}
