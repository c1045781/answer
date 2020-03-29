package top.qiyoung.answer.enums;

public enum UserRoleEnum {
    SUPER_MANAGER(0, "超级管理员"),
    MANAGER(1, "管理员"),
    GENERAL_USER(2, "一般用户"),
    SPECIAL_USER(3, "特殊用户");
    private int type;
    private String name;


    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    UserRoleEnum(int status, String name) {
        this.type = status;
        this.name = name;
    }
}
