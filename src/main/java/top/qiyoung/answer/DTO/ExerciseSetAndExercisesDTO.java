package top.qiyoung.answer.DTO;

import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.ExerciseSet;
import top.qiyoung.answer.model.Subject;

import java.util.List;

public class ExerciseSetAndExercisesDTO {
    private ExerciseSet exerciseSet;
    private List<ExerciseReviewDTO> list;

    public ExerciseSetAndExercisesDTO() {
    }

    public ExerciseSetAndExercisesDTO(ExerciseSet exerciseSet, List<ExerciseReviewDTO> list) {
        this.exerciseSet = exerciseSet;
        this.list = list;
    }

    public ExerciseSet getExerciseSet() {
        return exerciseSet;
    }

    public void setExerciseSet(ExerciseSet exerciseSet) {
        this.exerciseSet = exerciseSet;
    }

    public List<ExerciseReviewDTO> getList() {
        return list;
    }

    public void setList(List<ExerciseReviewDTO> list) {
        this.list = list;
    }
}
