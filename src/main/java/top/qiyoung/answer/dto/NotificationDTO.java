package top.qiyoung.answer.dto;

import top.qiyoung.answer.model.Message;
import top.qiyoung.answer.model.MyUser;

import java.util.Date;

public class NotificationDTO {
    private Integer notificationId;
    private MyUser notifier;
    private MyUser receiver;
    private Integer type;
    private Date createTime;
    private Integer status;
    private String title;
    private Message message;

    public NotificationDTO() {
    }

    public NotificationDTO(Integer notificationId, MyUser notifier, MyUser receiver, Integer type, Date createTime, Integer status, String title, Message message) {
        this.notificationId = notificationId;
        this.notifier = notifier;
        this.receiver = receiver;
        this.type = type;
        this.createTime = createTime;
        this.status = status;
        this.title = title;
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public MyUser getNotifier() {
        return notifier;
    }

    public void setNotifier(MyUser notifier) {
        this.notifier = notifier;
    }

    public MyUser getReceiver() {
        return receiver;
    }

    public void setReceiver(MyUser receiver) {
        this.receiver = receiver;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
