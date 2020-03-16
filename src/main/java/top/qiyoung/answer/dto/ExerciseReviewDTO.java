package top.qiyoung.answer.dto;

import top.qiyoung.answer.model.*;

import java.util.List;

public class ExerciseReviewDTO {
    private Exercise exercise;
    private Message message;
    private Subject subject;
    private MyUser myUser;
    private List<Option> options;

    public ExerciseReviewDTO() {
    }

    public ExerciseReviewDTO(Exercise exercise, Message message, Subject subject, MyUser myUser, List<Option> options) {
        this.exercise = exercise;
        this.message = message;
        this.subject = subject;
        this.myUser = myUser;
        this.options = options;
    }

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
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
