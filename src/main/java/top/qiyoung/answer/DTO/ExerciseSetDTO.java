package top.qiyoung.answer.DTO;

import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.Subject;
import top.qiyoung.answer.model.User;

import java.util.Date;
import java.util.List;

public class ExerciseSetDTO {
    private Integer exerciseSetId;
//    private Integer subjectId;
    private String title;
//    private List<Integer> exerciseIds;
    private Date createTime;
    private Subject subject;
    private List<Subject> subjectList;
    private List<String> baseList;
    private List<Exercise> exerciseList;
    private User user;

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

    public Integer getExerciseSetId() {
        return exerciseSetId;
    }

    public void setExerciseSetId(Integer exerciseSetId) {
        this.exerciseSetId = exerciseSetId;
    }

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }


//    public Integer getSubjectId() {
//        return subjectId;
//    }
//
//    public void setSubjectId(Integer subjectId) {
//        this.subjectId = subjectId;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public List<Integer> getExerciseIds() {
//        return exerciseIds;
//    }
//
//    public void setExerciseIds(List<Integer> exerciseIds) {
//        this.exerciseIds = exerciseIds;
//    }
}
