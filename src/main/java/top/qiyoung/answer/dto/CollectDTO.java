package top.qiyoung.answer.dto;

import top.qiyoung.answer.model.Collect;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.Subject;

public class CollectDTO {
    private Collect collect;
    private Exercise exercise;
    private Subject subject;

    public CollectDTO() {
    }

    public CollectDTO(Collect collect, Exercise exercise, Subject subject) {
        this.collect = collect;
        this.exercise = exercise;
        this.subject = subject;
    }

    public Collect getCollect() {
        return collect;
    }

    public void setCollect(Collect collect) {
        this.collect = collect;
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
}
