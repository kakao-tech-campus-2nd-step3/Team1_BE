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
        optionService.createOption(new OptionCreate(
            "진행률에 따른 나무 성장!",
            "진행률이 오를수록 나무가 성장해요.",
            "treeGrowth"));
        optionService.createOption(new OptionCreate(
            "진행률에 따른 빵빠레!",
            "진행률이 50% 달성될 때 메인 화면에 빵빠레가 울려요!",
            "celebration"));
        optionService.createOption(new OptionCreate(
            "마감 기한에 따른 색 변화!",
            "마감 기한이 1일 남았을 때 아이콘이 빨간색으로 바뀌어요!",
            "colorChange"));
        optionService.createOption(new OptionCreate(
            "이메일 전송!",
            "마감기한이 3일 남았을 때 하루 간격으로 이메일이 전송돼요!",
            "emailSend"));
    }
}
