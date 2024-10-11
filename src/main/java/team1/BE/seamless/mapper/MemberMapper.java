package team1.BE.seamless.mapper;

import org.springframework.stereotype.Component;
import team1.BE.seamless.DTO.MemberRequestDTO.CreateMember;
import team1.BE.seamless.DTO.MemberRequestDTO.UpdateMember;
import team1.BE.seamless.DTO.MemberResponseDTO;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.ProjectEntity;

@Component
public class MemberMapper {

    public MemberEntity toEntity(CreateMember create, ProjectEntity project) {

        return new MemberEntity(
            create.getName(),
            create.getRole(),
            create.getEmail(),
            create.getImageURL(),
            project
        );
    }

    public MemberEntity toUpdate(MemberEntity member, UpdateMember update) {
        member.setEmail(update.getEmail());
        member.setImageURL(update.getImageURL());
        member.setRole(update.getRole());
        member.setName(update.getName());
        return member;
    }

    public MemberResponseDTO toGetResponseDTO(MemberEntity memberEntity) {
        return new MemberResponseDTO("성공적으로 조회되었습니다.",
                memberEntity.getName(),
                memberEntity.getRole(),
                memberEntity.getEmail());
    }

    public MemberResponseDTO toDeleteResponseDTO(MemberEntity memberEntity) {
        return new MemberResponseDTO("성공적으로 삭제되었습니다.",
                memberEntity.getName(),
                memberEntity.getRole(),
                memberEntity.getEmail());
    }

    public MemberResponseDTO toCreateResponseDTO(MemberEntity memberEntity) {
        return new MemberResponseDTO("성공적으로 생성되었습니다.",
                memberEntity.getName(),
                memberEntity.getRole(),
                memberEntity.getEmail());
    }

    public MemberResponseDTO toPutResponseDTO(MemberEntity memberEntity) {
        return new MemberResponseDTO("성공적으로 수정되었습니다.",
                memberEntity.getName(),
                memberEntity.getRole(),
                memberEntity.getEmail());
    }

}
