package top.qiyoung.answer.model;

import java.util.Date;

public class Comment{
    private Integer id;
    private Integer userId;
    private Integer receiverId;
    private Integer parentId;
    private Integer likeCount;
    private Integer type;
    private String content;
    private Date createTime;

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Comment() {
    }

    public Comment(Integer id, Integer userId, Integer receiverId, Integer parentId, Integer likeCount, Integer type, String content, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.receiverId = receiverId;
        this.parentId = parentId;
        this.likeCount = likeCount;
        this.type = type;
        this.content = content;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
