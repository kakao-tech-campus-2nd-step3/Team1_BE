package team1.be.seamless.entity.enums;

public enum Role {
    ADMIN("ADMIN", "관리자"),
    USER("USER", "사용자"),
    MEMBER("MEMBER", "프로젝트 멤버");

    private final String key;
    private final String title;

    Role(String key, String title) {
        this.key = key;
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public boolean isRole(String role) {
        return this.key.equals(role);
    }
}
