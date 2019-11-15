package top.qiyoung.answer.model;

public class ExercisesVM {
    private Exercises exercises;
    private Subject subject;
    private User user;
    private ExercisesContentVM exercisesContentVM;


    public Exercises getExercises() {
        return exercises;
    }

    public void setExercises(Exercises exercises) {
        this.exercises = exercises;
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

    public ExercisesContentVM getExercisesContentVM() {
        return exercisesContentVM;
    }

    public void setExercisesContentVM(ExercisesContentVM exercisesContentVM) {
        this.exercisesContentVM = exercisesContentVM;
    }
}
