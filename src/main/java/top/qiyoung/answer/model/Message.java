package top.qiyoung.answer.model;

import java.util.Date;

public class Message {
    private Integer messageId;
    private Integer exerciseId;
    private String content;
    private Integer type;
    private String reason;
    private Integer userId;
    private Date createTime;
    private Integer status;


    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Message(Integer messageId, Integer exerciseId, String content, Integer type, String reason, Integer userId, Date createTime, Integer status) {
        this.messageId = messageId;
        this.exerciseId = exerciseId;
        this.content = content;
        this.type = type;
        this.reason = reason;
        this.userId = userId;
        this.createTime = createTime;
        this.status = status;
    }

    public Message() {
    }
}
