package top.qiyoung.answer.model;

import java.util.Date;

public class Message {
    private Integer messageId;
    private String content;
    private Integer status;
    private Date createTime;
    private Integer userId;
    private String reason;

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

    public Message(Integer messageId, String content, Integer status, Date createTime, Integer userId,String reason) {
        this.messageId = messageId;
        this.content = content;
        this.status = status;
        this.createTime = createTime;
        this.userId = userId;
        this.reason = reason;
    }

    public Message() {
    }
}
