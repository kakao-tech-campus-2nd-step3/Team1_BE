package team1.BE.seamless.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import team1.BE.seamless.DTO.OptionDTO.OptionCreate;
import team1.BE.seamless.DTO.OptionDTO.OptionDetail;
import team1.BE.seamless.DTO.OptionDTO.OptionSimple;
import team1.BE.seamless.DTO.OptionDTO.OptionUpdate;
import team1.BE.seamless.entity.OptionEntity;
import team1.BE.seamless.util.Util;
import team1.BE.seamless.util.errorException.BaseHandler;

@Component
public class OptionMapper {

    public OptionEntity toEntity(OptionCreate create) {
        return new OptionEntity(
            create.getName(),
            create.getDescription(),
            create.getOptionType()
        );
    }

    public OptionEntity toUpdate(OptionEntity entity, OptionUpdate update) {
        return entity.Update(
            Util.isNull(update.getName()) ? entity.getName() : update.getName(),
            Util.isNull(update.getDescription())? entity.getDescription() : update.getDescription(),
            Util.isNull(update.getOptionType())? entity.getOptionType() : update.getOptionType()
        );
    }


    public OptionSimple toSimple(OptionEntity entity) {
        return new OptionSimple(entity.getId(), entity.getName(), entity.getOptionType());
    }

    public OptionDetail toDetail(OptionEntity entity) {
        return new OptionDetail(entity.getId(), entity.getName(), entity.getDescription(),
            entity.getOptionType(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
