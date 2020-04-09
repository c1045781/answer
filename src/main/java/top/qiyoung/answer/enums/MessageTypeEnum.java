package top.qiyoung.answer.enums;

public enum MessageTypeEnum {
    PERMISSION_APPLICATION(1, "权限申请"),
    EXERCISES_APPLICATION(2, "试题申请"),
    NOTICE(3,"公告"),
    NOTICE_GENERAL(4,"一般用户公告"),
    NOTICE_SPECIAL(5,"特殊用户公告");
    private int type;
    private String name;


    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    MessageTypeEnum(int status, String name) {
        this.type = status;
        this.name = name;
    }
}
