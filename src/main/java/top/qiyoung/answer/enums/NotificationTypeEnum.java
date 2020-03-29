package top.qiyoung.answer.enums;

public enum NotificationTypeEnum {
    NOTIFICATION_LIKE(1, "评论点赞"),
    NOTIFICATION_COMMENT(2, "评论回复"),
    NOTIFICATION_SYSTEM_EXERCISE(3, "试题申请"),
    NOTIFICATION_SYSTEM_PERMISSION(4,"权限申请"),
    NOTIFICATION_CHAT(5,"私信"),
    NOTIFICATION_NOTICE(6,"公告"),
    NOTIFICATION_NOTICE_GENERAL(7,"一般用户公告"),
    NOTIFICATION_NOTICE_SPECIAL(8,"特殊用户公告");
    private int type;
    private String name;


    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationTypeEnum(int status, String name) {
        this.type = status;
        this.name = name;
    }

    public static String nameOfType(int type) {
        for (NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
            if (notificationTypeEnum.getType() == type) {
                return notificationTypeEnum.getName();
            }
        }
        return "";
    }
}
