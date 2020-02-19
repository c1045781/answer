package top.qiyoung.answer.DTO;

import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.Option;
import top.qiyoung.answer.model.Subject;
import top.qiyoung.answer.model.User;

import java.util.List;

public class ExerciseDTO {
    private Exercise exercise;
    private Subject subject;
    private User user;
    private List<Option> options;

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
