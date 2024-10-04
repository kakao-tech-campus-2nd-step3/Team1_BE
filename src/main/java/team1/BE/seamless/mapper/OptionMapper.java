package team1.BE.seamless.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import team1.BE.seamless.DTO.OptionDTO.OptionCreate;
import team1.BE.seamless.entity.OptionEntity;
import team1.BE.seamless.entity.enums.OptionType;
import team1.BE.seamless.util.errorException.BaseHandler;

@Component
public class OptionMapper {

    public OptionEntity toEntity(OptionCreate create) {
        return new OptionEntity(
            create.getName(),
            create.getDescription(),
            toOptionType(create.getOptionType())
        );
    }

    public OptionType toOptionType(String optionType) {
        for (OptionType type : OptionType.values()) {
            if (type.getKey().equals(optionType)) {
                return type;
            }
        }
        throw new BaseHandler(HttpStatus.NOT_FOUND,"잘못된 옵션 타입입니다.");
    }
}
