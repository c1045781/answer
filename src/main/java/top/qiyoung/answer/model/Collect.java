package top.qiyoung.answer.model;

import java.util.Date;

public class Collect {
    private Integer collectId;
    private Integer userId;
    private Integer exerciseId;
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCollectId() {
        return collectId;
    }

    public void setCollectId(Integer collectId) {
        this.collectId = collectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Collect(Integer collectId, Integer userId, Integer exerciseId,Date createTime) {
        this.collectId = collectId;
        this.userId = userId;
        this.exerciseId = exerciseId;
        this.createTime = createTime;
    }

    public Collect(){

    }
}
