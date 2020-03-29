package top.qiyoung.answer.model;

import java.util.Date;

public class Notification {
    private Integer notificationId;
    private Integer notifierId;
    private Integer receiverId;
    private Integer outerId;
    private Integer type;
    private Date createTime;
    private Integer status;
    private String title;

    public Notification() {
    }

    public Notification(Integer notificationId, Integer notifierId, Integer receiverId, Integer outerId, Integer type, Date createTime, Integer status, String title) {
        this.notificationId = notificationId;
        this.notifierId = notifierId;
        this.receiverId = receiverId;
        this.outerId = outerId;
        this.type = type;
        this.createTime = createTime;
        this.status = status;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getNotifierId() {
        return notifierId;
    }

    public void setNotifierId(Integer notifierId) {
        this.notifierId = notifierId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getOuterId() {
        return outerId;
    }

    public void setOuterId(Integer outerId) {
        this.outerId = outerId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
