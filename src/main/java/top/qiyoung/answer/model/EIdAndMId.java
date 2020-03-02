package top.qiyoung.answer.model;

public class EIdAndMId {
    private Integer exerciseId;
    private Integer messageId;

    public EIdAndMId() {
    }

    public EIdAndMId(Integer exerciseId, Integer messageId) {
        this.exerciseId = exerciseId;
        this.messageId = messageId;
    }

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }
}
