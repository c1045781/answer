package top.qiyoung.answer.enums;

public enum NotificationStatusEnum {
    UNREAD(0, "未读"),
    READ(1, "已读");
    private int type;
    private String name;


    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationStatusEnum(int status, String name) {
        this.type = status;
        this.name = name;
    }
}
