package team1.BE.seamless.DTO;

import java.util.List;

public class OptionDTO {

    private List<String> optionNames;

    public OptionDTO(List<String> optionNames) {
        this.optionNames = optionNames;
    }

    public List<String> getOptionNames() {
        return optionNames;
    }

    public void setOptionNames(List<String> optionNames) {
        this.optionNames = optionNames;
    }

}
