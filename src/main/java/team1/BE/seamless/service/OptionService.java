package team1.BE.seamless.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team1.BE.seamless.DTO.OptionDTO.OptionCreate;
import team1.BE.seamless.entity.OptionEntity;
import team1.BE.seamless.mapper.OptionMapper;
import team1.BE.seamless.repository.OptionRepository;

@Service
public class OptionService {

    private OptionRepository optionRepository;

    private OptionMapper optionMapper;

    @Autowired
    public OptionService(OptionRepository optionRepository, OptionMapper optionMapper) {
        this.optionRepository = optionRepository;
        this.optionMapper = optionMapper;
    }

    @Transactional
    public OptionEntity createOption(OptionCreate create) {
        return optionRepository.save(optionMapper.toEntity(create));
    }
}
