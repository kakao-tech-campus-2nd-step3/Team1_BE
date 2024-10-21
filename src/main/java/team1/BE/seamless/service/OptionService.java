package team1.BE.seamless.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team1.BE.seamless.DTO.OptionDTO.OptionCreate;
import team1.BE.seamless.DTO.OptionDTO.OptionDetail;
import team1.BE.seamless.DTO.OptionDTO.OptionSimple;
import team1.BE.seamless.DTO.OptionDTO.getList;
import team1.BE.seamless.DTO.OptionDTO.updateOption;
import team1.BE.seamless.entity.OptionEntity;
import team1.BE.seamless.entity.enums.Role;
import team1.BE.seamless.mapper.OptionMapper;
import team1.BE.seamless.repository.OptionRepository;
import team1.BE.seamless.util.errorException.BaseHandler;

@Service
public class OptionService {

    private OptionRepository optionRepository;

    private OptionMapper optionMapper;

    @Autowired
    public OptionService(OptionRepository optionRepository, OptionMapper optionMapper) {
        this.optionRepository = optionRepository;
        this.optionMapper = optionMapper;
    }

    public Page<OptionSimple> getProjectOptionList(getList param, String role) {
        if (!role.equals(Role.USER.getKey())) {
            throw new BaseHandler(HttpStatus.FORBIDDEN, "로그인한 유저만 조회 가능합니다.");
        }

        return optionRepository.findAllByIsDeletedFalse(param.toPageable()).map(optionMapper::toSimple);
    }

    public OptionDetail getOption(Long id, String role) {
        if (!role.equals(Role.USER.getKey())) {
            throw new BaseHandler(HttpStatus.FORBIDDEN, "로그인한 유저만 조회 가능합니다.");
        }

        return optionMapper.toDetail(optionRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당하는 옵션이 존재하지 않습니다.")));
    }

    @Transactional
    public OptionDetail createOption(OptionCreate create, String role) {
        if (!role.equals(Role.USER.getKey())) {
            throw new BaseHandler(HttpStatus.FORBIDDEN, "로그인한 유저만 생성 가능합니다.");
        }
        OptionEntity optionEntity = optionMapper.toEntity(create);
        optionRepository.save(optionEntity);

        return optionMapper.toDetail(optionEntity);
    }

    @Transactional
    public OptionDetail updateOption(Long id, updateOption update, String role) {
        if (!role.equals(Role.USER.getKey())) {
            throw new BaseHandler(HttpStatus.FORBIDDEN, "로그인한 유저만 수정 가능합니다.");
        }
        OptionEntity option = optionRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당하는 옵션이 존재하지 않습니다."));

        optionMapper.toUpdate(option, update);

        return optionMapper.toDetail(option);
    }

    @Transactional
    public OptionDetail deleteOption(Long id, String role) {
        if (!role.equals(Role.USER.getKey())) {
            throw new BaseHandler(HttpStatus.FORBIDDEN, "로그인한 유저만 삭제 가능합니다.");
        }

        OptionEntity option = optionRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당하는 옵션이 존재하지 않습니다."));

        option.setIsDeleted(true);

        return optionMapper.toDetail(option);
    }

    /**
     * 테스트용
     */
    @Transactional
    public OptionEntity createOption(OptionCreate create) {
        return optionRepository.save(optionMapper.toEntity(create));
    }

}
