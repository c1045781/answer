package top.qiyoung.answer.enums;

public enum CommentTypeEnum {
    FIRST_COMMENT(1, "一级评论"),
    SECOND_COMMENT(2, "二级评论");
    private int type;
    private String name;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    CommentTypeEnum(int status, String name) {
        this.type = status;
        this.name = name;
    }

}
