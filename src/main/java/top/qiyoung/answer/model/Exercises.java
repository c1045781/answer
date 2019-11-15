package top.qiyoung.answer.model;

import java.util.Date;

public class Exercises {
    private Integer id;
    private String exercisesType;
    private Integer subjectId;
    private String correct;
    private Integer exercisesContentId;
    private Integer createUserId;
    private Integer status;
    private Date createTime;
    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExercisesType() {
        return exercisesType;
    }

    public void setExercisesType(String exercisesType) {
        this.exercisesType = exercisesType;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public Integer getExercisesContentId() {
        return exercisesContentId;
    }

    public void setExercisesContentId(Integer exercisesContentId) {
        this.exercisesContentId = exercisesContentId;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
