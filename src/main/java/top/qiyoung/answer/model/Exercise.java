package top.qiyoung.answer.model;

import java.util.Date;

public class Exercise {
    private Integer exerciseId;
    private String exerciseType;
    private String correct;
    private String exerciseTitle;
    private String optionContent;
    private Integer createUserId;
    private Integer status;
    private Date createTime;
    private Date modifyTime;
    private Integer subjectId;
    private String analysis;
    private Integer score;
    private Integer people;

    public Exercise(Integer exerciseId, String exerciseType, String correct, String exerciseTitle, String optionContent, Integer createUserId, Integer status, Date createTime, Date modifyTime, Integer subjectId, String analysis, Integer score, Integer people) {
        this.exerciseId = exerciseId;
        this.exerciseType = exerciseType;
        this.correct = correct;
        this.exerciseTitle = exerciseTitle;
        this.optionContent = optionContent;
        this.createUserId = createUserId;
        this.status = status;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.subjectId = subjectId;
        this.analysis = analysis;
        this.score = score;
        this.people = people;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getPeople() {
        return people;
    }

    public void setPeople(Integer people) {
        this.people = people;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
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

    public String getExerciseTitle() {
        return exerciseTitle;
    }

    public void setExerciseTitle(String exerciseTitle) {
        this.exerciseTitle = exerciseTitle;
    }

    public String getOptionContent() {
        return optionContent;
    }

    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
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

    public Exercise(Integer exerciseId, String exerciseType, String correct, String exerciseTitle, String optionContent, Integer createUserId, Integer status, Date createTime, Date modifyTime, Integer subjectId,String analysis) {
        this.exerciseId = exerciseId;
        this.exerciseType = exerciseType;
        this.correct = correct;
        this.exerciseTitle = exerciseTitle;
        this.optionContent = optionContent;
        this.createUserId = createUserId;
        this.status = status;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.subjectId = subjectId;
        this.analysis = analysis;
    }

    public Exercise() {
    }
}
