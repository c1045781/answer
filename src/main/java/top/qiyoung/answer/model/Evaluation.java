package top.qiyoung.answer.model;

public class Evaluation {
    private Integer evaluationId;
    private Integer userId;
    private Integer exerciseId;
    private Integer score;

    public Evaluation() {
    }

    public Evaluation(Integer evaluationId, Integer userId, Integer exerciseId, Integer score) {
        this.evaluationId = evaluationId;
        this.userId = userId;
        this.exerciseId = exerciseId;
        this.score = score;
    }

    public Integer getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Integer evaluationId) {
        this.evaluationId = evaluationId;
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
