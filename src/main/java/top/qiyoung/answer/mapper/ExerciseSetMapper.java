package top.qiyoung.answer.mapper;

import top.qiyoung.answer.model.ExerciseSet;
import top.qiyoung.answer.model.Query;

import java.util.List;

public interface ExerciseSetMapper {
    int insert(ExerciseSet exerciseSet);

//    List<ExerciseSet> getExerciseList();

    List<ExerciseSet> getExerciseSetList(Query query);

    int countExerciseSetList(Query query);

    int delete(Integer exerciseSetId);

    Integer getLastId();

    ExerciseSet getExerciseSetById(Integer exerciseSetId);

    int update(ExerciseSet exerciseSet);

    int deleteByUserId(Integer userId);
}
