package top.qiyoung.answer.model;

import java.util.Date;
import java.util.List;

public class ExerciseSet {
    private Integer exerciseSetId;
    private String title;
    private Integer exerciseCount;
    private Integer createUserId;
    private Date createTime;
    private Date modifyTime;
    List<Exercise> exerciseList;
    private Integer subjectId;

    public Integer getExerciseSetId() {
        return exerciseSetId;
    }

    public void setExerciseSetId(Integer exerciseSetId) {
        this.exerciseSetId = exerciseSetId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }


    public Integer getExerciseCount() {
        return exerciseCount;
    }

    public void setExerciseCount(Integer exerciseCount) {
        this.exerciseCount = exerciseCount;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }


    public ExerciseSet(Integer exerciseSetId, String title, Integer exerciseCount, Integer createUserId, Date createTime, Date modifyTime, List<Exercise> exerciseList, Integer subjectId) {
        this.exerciseSetId = exerciseSetId;
        this.title = title;
        this.exerciseCount = exerciseCount;
        this.createUserId = createUserId;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.exerciseList = exerciseList;
        this.subjectId = subjectId;
    }

    public ExerciseSet() {
    }
}
