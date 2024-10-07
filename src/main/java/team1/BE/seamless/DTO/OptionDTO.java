package team1.BE.seamless.DTO;

public class OptionDTO {

    public static class OptionCreate {

        private String name;

        private String eventType;

        public OptionCreate() {

        }

        public OptionCreate(String name, String eventType) {
            this.name = name;
            this.eventType = eventType;
        }

        public String getName() {
            return name;
        }

        public String getEventType() {
            return eventType;
        }

    }

}
