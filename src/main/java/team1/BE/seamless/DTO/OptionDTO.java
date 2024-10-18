package team1.BE.seamless.DTO;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import team1.BE.seamless.entity.enums.OptionType;
import team1.BE.seamless.util.page.PageParam;

public class OptionDTO {
    public static class getList extends PageParam {

    }

    public static class OptionCreate {

        @NotNull
        private String name;

        private String description;

        @NotNull
        private String optionType;

        public OptionCreate() {

        }

        public OptionCreate(String name, String description, String optionType) {
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

        public String getOptionType() {
            return optionType;
        }
    }

    public static class updateOption {

        private String name;

        private String description;

        private String optionType;

        public updateOption() {

        }

        public updateOption(String name, String description, String optionType) {
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

        public String getOptionType() {
            return optionType;
        }

    }

    public static class OptionSimple{
        private Long id;
        private String name;
        private String optionType;

        public OptionSimple() {
        }

        public OptionSimple(Long id, String name, OptionType optionType) {
            this.id = id;
            this.name = name;
            this.optionType = optionType.toString();
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getOptionType() {
            return optionType;
        }
    }

    public static class OptionDetail{
        private Long id;
        private String name;
        private String description;
        private String optionType;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public OptionDetail() {
        }

        public OptionDetail(Long id, String name, String description, OptionType optionType,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.optionType = optionType.toString();
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getOptionType() {
            return optionType;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }

}
