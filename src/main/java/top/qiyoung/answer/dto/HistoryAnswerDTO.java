package top.qiyoung.answer.dto;

import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.Subject;

import java.util.Date;

public class HistoryAnswerDTO {
    private Integer answerId;
    private Integer userId;
    private String answer;
    private Exercise exercise;
    private Subject subject;
    private Date createTime;

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public HistoryAnswerDTO() {
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public HistoryAnswerDTO(Integer answerId, Integer userId, String answer, Exercise exercise, Subject subject, Date createTime) {
        this.answerId = answerId;
        this.userId = userId;
        this.answer = answer;
        this.exercise = exercise;
        this.subject = subject;
        this.createTime = createTime;
    }
}
