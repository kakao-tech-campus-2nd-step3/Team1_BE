package team1.BE.seamless.mapper;

import org.springframework.stereotype.Component;
import team1.BE.seamless.DTO.OptionDTO.OptionCreate;
import team1.BE.seamless.entity.OptionEntity;

@Component
public class OptionMapper {

    public OptionEntity toEntity(OptionCreate create) {
        return new OptionEntity(
            create.getName(),
            create.getDescription(),
            create.getOptionType()
        );
    }
}
