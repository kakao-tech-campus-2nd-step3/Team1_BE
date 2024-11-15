package team1.be.seamless.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import team1.be.seamless.dto.OptionDTO.OptionCreate;
import team1.be.seamless.dto.OptionDTO.OptionDetail;
import team1.be.seamless.dto.OptionDTO.OptionSimple;
import team1.be.seamless.dto.OptionDTO.OptionUpdate;
import team1.be.seamless.dto.OptionDTO.getList;
import team1.be.seamless.entity.OptionEntity;
import team1.be.seamless.entity.enums.Role;
import team1.be.seamless.mapper.OptionMapper;
import team1.be.seamless.repository.OptionRepository;
import team1.be.seamless.util.errorException.BaseHandler;

@ExtendWith(MockitoExtension.class)
public class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private OptionMapper optionMapper;

    @InjectMocks
    private OptionService optionService;

    private getList param;

    private String role;

    private OptionEntity optionEntity1;

    private OptionEntity optionEntity2;

    @BeforeEach
    void setUp() {
        param = new getList();
        role = Role.ADMIN.getKey();
        optionEntity1 = new OptionEntity("옵션1", "옵션 설명1", "타입1");
        optionEntity2 = new OptionEntity("옵션2", "옵션 설명2", "타입2");
    }

    @Test
    void 옵션_리스트_조회_검증() {
        Page<OptionEntity> optionEntities = new PageImpl<>(
                List.of(optionEntity1, optionEntity2),
                param.toPageable(),
                param.getSize()
        );

        OptionSimple optionSimple1 = new OptionSimple(
                1L,
                "옵션1",
                "타입1"
        );

        OptionSimple optionSimple2 = new OptionSimple(
                2L,
                "옵션2",
                "타입2"
        );

        given(optionRepository.findAllByIsDeletedFalse(param.toPageable())).willReturn(optionEntities);
        given(optionMapper.toSimple(optionEntity1)).willReturn(optionSimple1);
        given(optionMapper.toSimple(optionEntity2)).willReturn(optionSimple2);

        Page<OptionSimple> result = optionService.getProjectOptionList(param, role);

        assertThat(result).isNotNull();
        then(optionRepository).should().findAllByIsDeletedFalse(param.toPageable());
        then(optionMapper).should().toSimple(optionEntity1);
        then(optionMapper).should().toSimple(optionEntity2);
    }

    @Test
    void 옵션_조회_검증() {

        Long optionId = 1L;
        OptionDetail optionDetail = new OptionDetail(
                optionId,
                "옵션1",
                "옵션 설명1",
                "타입1",
                optionEntity1.getCreatedAt(),
                optionEntity1.getUpdatedAt()
        );
        given(optionRepository.findByIdAndIsDeletedFalse(optionId)).willReturn(Optional.of(optionEntity1));
        given(optionMapper.toDetail(optionEntity1)).willReturn(optionDetail);

        OptionDetail result = optionService.getOption(optionId, role);

        assertThat(result).isNotNull();
        then(optionRepository).should().findByIdAndIsDeletedFalse(optionId);
        then(optionMapper).should().toDetail(optionEntity1);
    }

    @Test
    void 옵션_생성_검증() {

        Long optionId = 1L;
        OptionCreate create = new OptionCreate("옵션1", "옵션 설명1", "타입1");

        OptionDetail optionDetail = new OptionDetail(
                optionId,
                "옵션1",
                "옵션 설명1",
                "타입1",
                optionEntity1.getCreatedAt(),
                optionEntity1.getUpdatedAt()
        );

        given(optionMapper.toEntity(create)).willReturn(optionEntity1);
        given(optionMapper.toDetail(optionEntity1)).willReturn(optionDetail);

        OptionDetail result = optionService.createOption(create, role);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("옵션1");
        assertThat(result.getDescription()).isEqualTo("옵션 설명1");
        assertThat(result.getOptionType()).isEqualTo("타입1");
        then(optionRepository).should().save(optionEntity1);
        then(optionMapper).should().toEntity(create);
        then(optionMapper).should().toDetail(optionEntity1);
    }

    @Test
    void 옵션_수정_검증() {

        Long optionId = 1L;
        OptionUpdate update = new OptionUpdate("옵션2", "옵션 설명2", "타입1");

        OptionEntity updatedOption = new OptionEntity("옵션2", "옵션 설명2", "타입1");

        OptionDetail optionDetail = new OptionDetail(
                optionId,
                "옵션2",
                "옵션 설명2",
                "타입1",
                optionEntity1.getCreatedAt(),
                optionEntity1.getUpdatedAt()
        );

        given(optionRepository.findByIdAndIsDeletedFalse(optionId)).willReturn(Optional.of(optionEntity1));
        given(optionMapper.toUpdate(optionEntity1, update)).willReturn(updatedOption);
        given(optionMapper.toDetail(optionEntity1)).willReturn(optionDetail);

        OptionDetail result = optionService.updateOption(1L, update, role);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("옵션2");
        assertThat(result.getDescription()).isEqualTo("옵션 설명2");
        assertThat(result.getOptionType()).isEqualTo("타입1");
        then(optionRepository).should().findByIdAndIsDeletedFalse(optionId);
        then(optionMapper).should().toUpdate(optionEntity1, update);
        then(optionMapper).should().toDetail(optionEntity1);
    }

    @Test
    void 옵션_삭제_검증() {

        Long optionId = 1L;
        OptionDetail optionDetail = new OptionDetail(
                optionId,
                "옵션1",
                "옵션 설명1",
                "타입1",
                optionEntity1.getCreatedAt(),
                optionEntity1.getUpdatedAt()
        );
        given(optionRepository.findByIdAndIsDeletedFalse(optionId)).willReturn(Optional.of(optionEntity1));
        given(optionMapper.toDetail(optionEntity1)).willReturn(optionDetail);

        OptionDetail result = optionService.deleteOption(optionId, role);

        assertThat(result).isNotNull();
        assertThat(optionEntity1.getIsDeleted()).isTrue();
        then(optionRepository).should().findByIdAndIsDeletedFalse(optionId);
        then(optionMapper).should().toDetail(optionEntity1);
    }

}
