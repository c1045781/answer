package top.qiyoung.answer.dto;

import top.qiyoung.answer.model.Option;
import top.qiyoung.answer.model.Subject;

import java.util.List;

public class ExerciseEditDTO {
    private Integer exerciseEditId;
    private String exerciseType;
    private List<String> baseList;
    private Integer subjectId;
    private List<Subject> subjectList;
    private String correct;
    private List<Option> options;
    private String title;
    private List<String> answers;
    private String analysis;


    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

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

    public ExerciseEditDTO(Integer exerciseEditId, String exerciseType, List<String> baseList, Integer subjectId, List<Subject> subjectList, String correct, List<Option> options, String title, List<String> answers, String analysis) {
        this.exerciseEditId = exerciseEditId;
        this.exerciseType = exerciseType;
        this.baseList = baseList;
        this.subjectId = subjectId;
        this.subjectList = subjectList;
        this.correct = correct;
        this.options = options;
        this.title = title;
        this.answers = answers;
        this.analysis = analysis;
    }

    public ExerciseEditDTO() {
    }
}
