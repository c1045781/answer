package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.Query;

import java.util.List;

public interface ExerciseMapper {
    int insert(Exercise exercise);

    List<Exercise> getExerciseList(Query query);

    int countExerciseList(Query query);

    int deleteById(Integer id);

    Exercise getExerciseById(Integer id);

    int update(Exercise exercise);

    List<Exercise> getExerciseBySubjectId(@Param("query") Query query,@Param("subjectId") Integer subjectId);


    List<Exercise> getExerciseListBySubjectId(Integer subjectId);

    int deleteByUserId(Integer userId);

    List<Exercise> getExerciseByReview(Query query);

    int countExerciseByReview(Query query);

    int updateById(@Param("id") Integer id,@Param("status") Integer status);
}
