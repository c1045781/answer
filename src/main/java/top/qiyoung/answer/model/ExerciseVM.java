package top.qiyoung.answer.model;

public class ExerciseVM {
    private Exercise exercise;
    private Subject subject;
    private User user;
    private ExerciseContentVM exerciseContentVM;


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

    public ExerciseContentVM getExerciseContentVM() {
        return exerciseContentVM;
    }

    public void setExerciseContentVM(ExerciseContentVM exerciseContentVM) {
        this.exerciseContentVM = exerciseContentVM;
    }
}
