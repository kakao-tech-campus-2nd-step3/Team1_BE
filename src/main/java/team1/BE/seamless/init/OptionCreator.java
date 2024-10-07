package team1.BE.seamless.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team1.BE.seamless.DTO.OptionDTO.OptionCreate;
import team1.BE.seamless.service.OptionService;

@Component
public class OptionCreator {

    private final OptionService optionService;

    @Autowired
    public OptionCreator(OptionService optionService) {
        this.optionService = optionService;
    }

    public void creator() {
        optionService.createOption(new OptionCreate("옵션1", "타입1"));
        optionService.createOption(new OptionCreate("옵션2", "타입1"));
        optionService.createOption(new OptionCreate("옵션3", "타입2"));
    }
}
