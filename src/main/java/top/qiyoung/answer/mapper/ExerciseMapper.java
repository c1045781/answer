package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.Query;

import java.util.List;

public interface ExerciseMapper {
    void insert(Exercise exercise);

    List<Exercise> getExerciseList(Query query);

    int countExerciseList(Query query);

    void deleteById(Integer id);

    Exercise getExerciseById(Integer id);

    void update(Exercise exercise);

    List<Exercise> getExerciseBySubjectId(@Param("query") Query query,@Param("subjectId") Integer subjectId);
}
