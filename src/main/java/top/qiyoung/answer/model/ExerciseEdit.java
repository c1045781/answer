package top.qiyoung.answer.model;

import java.util.List;

public class ExerciseEdit {
    private Integer exerciseEditId;
    private String exerciseType;
    private List<String> baseList;
    private Integer subjectId;
    private List<Subject> subjectList;
    private String correct;
    private List<Option> options;
    private String title;
    private List<String> answers;

    public Integer getExerciseEditId() {
        return exerciseEditId;
    }

    public void setExerciseEditId(Integer exerciseEditId) {
        this.exerciseEditId = exerciseEditId;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public List<String> getBaseList() {
        return baseList;
    }

    public void setBaseList(List<String> baseList) {
        this.baseList = baseList;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
