package team1.BE.seamless.entity.enums;

public enum OptionType {
    POSITIVE("POSITIVE_OPTION", "긍정적_옵션"),
    NEGATIVE("NEGATIVE_OPTION", "부정적_옵션");

    private final String key;
    private final String title;

    OptionType(String key, String title) {
        this.key = key;
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }
}
