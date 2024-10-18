package team1.BE.seamless.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import team1.BE.seamless.DTO.OptionDTO.OptionCreate;
import team1.BE.seamless.DTO.OptionDTO.OptionDetail;
import team1.BE.seamless.DTO.OptionDTO.OptionSimple;
import team1.BE.seamless.DTO.OptionDTO.updateOption;
import team1.BE.seamless.entity.OptionEntity;
import team1.BE.seamless.entity.enums.OptionType;
import team1.BE.seamless.util.Util;
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

    public OptionEntity toUpdate(OptionEntity entity, updateOption update) {
        return entity.Update(
            Util.isNull(update.getName()) ? entity.getName() : update.getName(),
            Util.isNull(update.getDescription())? entity.getDescription() : update.getDescription(),
            Util.isNull(update.getOptionType().toString())?entity.getOptionType():toOptionType(update.getOptionType())
        );
    }

    public OptionType toOptionType(String optionType) {
        for (OptionType type : OptionType.values()) {
            if (type.getKey().equals(optionType)) {
                return type;
            }
        }
        throw new BaseHandler(HttpStatus.NOT_FOUND, "잘못된 옵션 타입입니다.");
    }

    public OptionSimple toSimple(OptionEntity entity) {
        return new OptionSimple(entity.getId(), entity.getName(), entity.getOptionType());
    }

    public OptionDetail toDetail(OptionEntity entity) {
        return new OptionDetail(entity.getId(), entity.getName(), entity.getDescription(),
            entity.getOptionType(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
