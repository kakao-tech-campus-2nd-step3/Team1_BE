package team1.BE.seamless.DTO;

import team1.BE.seamless.entity.enums.OptionType;

public class OptionDTO {

    public static class OptionCreate {

        private String name;

        private String description;

        private OptionType optionType;

        public OptionCreate() {

        }

        public OptionCreate(String name, String description, OptionType optionType) {
            this.name = name;
            this.description = description;
            this.optionType = optionType;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public OptionType getOptionType() {
            return optionType;
        }

    }

}
