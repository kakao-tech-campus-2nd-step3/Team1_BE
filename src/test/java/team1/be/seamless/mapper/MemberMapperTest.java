package team1.be.seamless.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import team1.be.seamless.dto.MemberRequestDTO.CreateMember;
import team1.be.seamless.dto.MemberRequestDTO.UpdateMember;
import team1.be.seamless.dto.MemberResponseDTO;
import team1.be.seamless.entity.MemberEntity;
import team1.be.seamless.entity.ProjectEntity;

import static org.assertj.core.api.Assertions.assertThat;

class MemberMapperTest {

    private MemberMapper memberMapper;

    @BeforeEach
    void setUp() {
        memberMapper = new MemberMapper();
    }

    @Test
    void testToEntity() {

        CreateMember createMember = new CreateMember("test@example.com", "attendURL", "Test Name");
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(1L);

        MemberEntity memberEntity = memberMapper.toEntity(createMember, projectEntity);

        assertThat(memberEntity).isNotNull();
        assertThat(memberEntity.getName()).isEqualTo("Test Name");
        assertThat(memberEntity.getRole()).isEqualTo("팀원");
        assertThat(memberEntity.getEmail()).isEqualTo("test@example.com");
        assertThat(memberEntity.getProjectEntity()).isEqualTo(projectEntity);
    }

    @Test
    void testToUpdate() {

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setName("Old Name");
        memberEntity.setEmail("old@example.com");
        memberEntity.setRole("Old Role");
        memberEntity.setImageURL("oldImageURL");

        UpdateMember updateMember = new UpdateMember("New Name", "New Role", "new@example.com", "newImageURL");

        MemberEntity updatedEntity = memberMapper.toUpdate(memberEntity, updateMember);

        assertThat(updatedEntity).isNotNull();
        assertThat(updatedEntity.getName()).isEqualTo("New Name");
        assertThat(updatedEntity.getRole()).isEqualTo("New Role");
        assertThat(updatedEntity.getEmail()).isEqualTo("new@example.com");
        assertThat(updatedEntity.getImageURL()).isEqualTo("newImageURL");
    }

    @Test
    void testToGetResponseDTO() {

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setName("Test Name");
        memberEntity.setRole("Test Role");
        memberEntity.setEmail("test@example.com");
        memberEntity.setId(1L);

        MemberResponseDTO responseDTO = memberMapper.toGetResponseDTO(memberEntity);

        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getMessage()).isEqualTo("성공적으로 조회되었습니다.");
        assertThat(responseDTO.getName()).isEqualTo("Test Name");
        assertThat(responseDTO.getRole()).isEqualTo("Test Role");
        assertThat(responseDTO.getEmail()).isEqualTo("test@example.com");
        assertThat(responseDTO.getId()).isEqualTo(1L);
    }

    @Test
    void testToDeleteResponseDTO() {

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setName("Test Name");
        memberEntity.setRole("Test Role");
        memberEntity.setEmail("test@example.com");

        MemberResponseDTO responseDTO = memberMapper.toDeleteResponseDTO(memberEntity);

        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getMessage()).isEqualTo("성공적으로 삭제되었습니다.");
        assertThat(responseDTO.getName()).isEqualTo("Test Name");
        assertThat(responseDTO.getRole()).isEqualTo("Test Role");
        assertThat(responseDTO.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testToCreateResponseDTO() {

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setName("Test Name");
        memberEntity.setRole("Test Role");
        memberEntity.setEmail("test@example.com");
        String attendURL = "http://example.com/attend";

        MemberResponseDTO responseDTO = memberMapper.toCreateResponseDTO(memberEntity, attendURL);

        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getMessage()).isEqualTo("성공적으로 생성되었습니다.");
        assertThat(responseDTO.getName()).isEqualTo("Test Name");
        assertThat(responseDTO.getRole()).isEqualTo("Test Role");
        assertThat(responseDTO.getEmail()).isEqualTo("test@example.com");
        assertThat(responseDTO.getattendURL()).isEqualTo(attendURL);
    }

    @Test
    void testToPutResponseDTO() {

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setName("Test Name");
        memberEntity.setRole("Test Role");
        memberEntity.setEmail("test@example.com");

        MemberResponseDTO responseDTO = memberMapper.toPutResponseDTO(memberEntity);

        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getMessage()).isEqualTo("성공적으로 수정되었습니다.");
        assertThat(responseDTO.getName()).isEqualTo("Test Name");
        assertThat(responseDTO.getRole()).isEqualTo("Test Role");
        assertThat(responseDTO.getEmail()).isEqualTo("test@example.com");
    }
}
