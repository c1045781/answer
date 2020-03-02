package top.qiyoung.answer.DTO;

import top.qiyoung.answer.model.*;

import java.util.List;

public class ExerciseReviewDTO {
    private Exercise exercise;
    private Message message;
    private Subject subject;
    private User user;
    private List<Option> options;

    public ExerciseReviewDTO() {
    }

    public ExerciseReviewDTO(Exercise exercise, Message message, Subject subject, User user, List<Option> options) {
        this.exercise = exercise;
        this.message = message;
        this.subject = subject;
        this.user = user;
        this.options = options;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
