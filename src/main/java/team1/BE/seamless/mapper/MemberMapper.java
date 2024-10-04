package team1.BE.seamless.mapper;

import org.springframework.stereotype.Component;
import team1.BE.seamless.DTO.MemberRequestDTO;
import team1.BE.seamless.entity.MemberEntity;
import java.util.Optional;

@Component
public class MemberMapper {

    public MemberEntity toMemberEntity(MemberRequestDTO memberRequestDTO) {

        MemberEntity memberEntity = new MemberEntity(
                memberRequestDTO.getName(),
                memberRequestDTO.getRole(),
                memberRequestDTO.getEmail(),
                memberRequestDTO.getImageURL()
                );

        return memberEntity;
    }

    public MemberEntity toMemberUpdateEntity(MemberRequestDTO memberRequestDTO, Optional<MemberEntity> existingMemberEntity) {
        MemberEntity updatedMember = existingMemberEntity.get(); // 이걸 먼저 해야 정보가 먼저 변경됨.
        updatedMember.setName(memberRequestDTO.getName()); // 세터없이 구현이 어렵다고 판단함. -> 기존 멤버의 정보를 유지해야하기 때문.
        updatedMember.setRole(memberRequestDTO.getRole());
        updatedMember.setEmail(memberRequestDTO.getEmail());
        updatedMember.setImageURL(memberRequestDTO.getImageURL());
        return updatedMember;
    }

}
