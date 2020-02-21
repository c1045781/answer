package top.qiyoung.answer.model;

public class Collect {
    private Integer collectId;
    private Integer userId;
    private Integer exerciseId;

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

    public Collect(Integer collectId, Integer userId, Integer exerciseId) {
        this.collectId = collectId;
        this.userId = userId;
        this.exerciseId = exerciseId;
    }

    public Collect(){

    }
}
