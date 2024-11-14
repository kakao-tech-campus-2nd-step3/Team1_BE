package team1.be.seamless.mapper;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import team1.be.seamless.dto.OptionDTO;
import team1.be.seamless.dto.OptionDTO.OptionCreate;
import team1.be.seamless.dto.OptionDTO.OptionDetail;
import team1.be.seamless.dto.OptionDTO.OptionSimple;
import team1.be.seamless.dto.OptionDTO.OptionUpdate;
import team1.be.seamless.entity.OptionEntity;

public class OptionMapperTest {

    private OptionMapper optionMapper;

    private OptionEntity optionEntity1;

    @BeforeEach
    void setUp() {
        optionMapper = new OptionMapper();
        optionEntity1 = new OptionEntity("옵션1", "옵션 설명1", "타입1");
    }

    @Test
    void 생성_시_OptionCreate_에서_Entity_로_변환_검증() {
        // Given
        OptionDTO.OptionCreate create = new OptionCreate("옵션1", "옵션 설명1", "타입1");

        //When
        OptionEntity result = optionMapper.toEntity(create);

        //Then
        assertThat(result.getName()).isEqualTo("옵션1");
        assertThat(result.getDescription()).isEqualTo("옵션 설명1");
        assertThat(result.getOptionType()).isEqualTo("타입1");
    }

    @Test
    void 수정시_해당_Entity가_업데이트_되는지_검증() {
        // Given
        OptionDTO.OptionUpdate update = new OptionUpdate("옵션2", "옵션 설명2", "타입1");

        // When
        OptionEntity result = optionMapper.toUpdate(optionEntity1, update);

        //Then
        assertThat(result.getName()).isEqualTo("옵션2");
        assertThat(result.getDescription()).isEqualTo("옵션 설명2");
        assertThat(result.getOptionType()).isEqualTo("타입1");
    }

    @Test
    void OptionEntity가_OptionSimple로_반환_되는_지_검증() {
        // Given & When
        OptionSimple result = optionMapper.toSimple(optionEntity1);

        // Then
        assertThat(result.getName()).isEqualTo("옵션1");
        assertThat(result.getOptionType()).isEqualTo("타입1");
    }

    @Test
    void OptionEntity가_OptionDetail로_반환_되는_지_검증() {
        // Given & When
        OptionDetail result = optionMapper.toDetail(optionEntity1);

        // Then
        assertThat(result.getName()).isEqualTo("옵션1");
        assertThat(result.getOptionType()).isEqualTo("타입1");
        assertThat(result.getCreatedAt()).isEqualTo(optionEntity1.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(optionEntity1.getUpdatedAt());

    }

}
