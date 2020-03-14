package top.qiyoung.answer.DTO;

import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.MyUser;

import java.util.Date;

public class CommentDTO {
    private Integer commentId;
    private String content;
    private Exercise exercise;
    private MyUser myUser;
    private Date createDate;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
