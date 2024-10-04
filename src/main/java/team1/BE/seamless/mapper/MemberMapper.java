package team1.BE.seamless.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import team1.BE.seamless.DTO.MemberRequestDTO;
import team1.BE.seamless.DTO.MemberRequestDTO.CreateMember;
import team1.BE.seamless.DTO.MemberRequestDTO.UpdateMember;
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

}
