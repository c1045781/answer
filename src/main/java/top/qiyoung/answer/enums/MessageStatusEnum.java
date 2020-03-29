package top.qiyoung.answer.enums;

public enum MessageStatusEnum {
    UNAUDITED(0, "未审核"),
    AUDIT_FAILED(1, "审核未通过"),
    AUDIT_PASS(2, "审核通过");
    private int type;
    private String name;


    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    MessageStatusEnum(int status, String name) {
        this.type = status;
        this.name = name;
    }

}
