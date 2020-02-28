package top.qiyoung.answer.model;

import java.util.Date;

public class Note {
    private Integer noteId;
    private String content;
    private Integer exerciseId;
    private Integer userId;
    private Date createTime;
    private Date modifyTime;

    public Note() {
    }

    public Note(Integer noteId, String content, Integer exerciseId, Integer userId, Date createTime, Date modifyTime) {
        this.noteId = noteId;
        this.content = content;
        this.exerciseId = exerciseId;
        this.userId = userId;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
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

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
