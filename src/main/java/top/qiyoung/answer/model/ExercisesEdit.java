package top.qiyoung.answer.model;

import java.util.List;

public class ExercisesEdit {
    private Integer id;
    private String exercisesType;
    private List<String> baseList;
    private Integer subjectId;
    private List<Subject> subjectList;
    private String correct;
    private List<Option> options;
    private String title;
    private List<String> answers;

    public List<String> getBaseList() {
        return baseList;
    }

    public void setBaseList(List<String> baseList) {
        this.baseList = baseList;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExercisesType() {
        return exercisesType;
    }

    public void setExercisesType(String exercisesType) {
        this.exercisesType = exercisesType;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }
}
