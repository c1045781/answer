package top.qiyoung.answer.dto;

import top.qiyoung.answer.model.Comment;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.MyUser;

import java.util.Date;
import java.util.List;

public class CommentDTO {
    private Integer commentId;
    private String content;
    private Integer likeCount;
    private MyUser myUser;
    private MyUser receiver;
    private Date createDate;
    private PaginationDTO<CommentDTO> paginationDTO;
    private Exercise exercise;

    public MyUser getReceiver() {
        return receiver;
    }

    public void setReceiver(MyUser receiver) {
        this.receiver = receiver;
    }

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

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
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

    public PaginationDTO<CommentDTO> getPaginationDTO() {
        return paginationDTO;
    }

    public void setPaginationDTO(PaginationDTO<CommentDTO> paginationDTO) {
        this.paginationDTO = paginationDTO;
    }
}
