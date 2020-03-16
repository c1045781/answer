package top.qiyoung.answer.exception;

public enum CustomizeErrorCode {

    EXERCISE_NOT_FOUND(2001, "试题不存在，请重新操作"),
    COMMENT_NOT_FOUND(2002,"评论不存在，请重新操作"),
    MESSAGE_NOT_FOUND(2003,"申请不存在，请重新操作"),
    SYS_ERROR(2004, "服务错误，请重新操作"),
    EXERCISE_SET_NOT_FOUND(2005, "套题不存在，请重新操作"),
    USER_NOT_FOUND(2006, "用户不存在，请重新操作"),
    SUBJECT_NOT_FOUND(2007, "分类不存在，请重新操作"),
    FILE_UPLOAD_FAIL(2008, "文件上传失败，请重新操作"),
    ANSWER_NOT_FOUND(2008, "历史答题不存在，请重新操作"),
    NOTE_NOT_FOUND(2008, "笔记不存在，请重新操作"),
    Insufficient_Permissions(2009, "权限不足，请重新操作"),
    ;


    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
