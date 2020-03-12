package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import top.qiyoung.answer.model.EIdAndMId;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.Query;

import java.util.List;

public interface ExerciseMapper {
    int insert(Exercise exercise);

   /* List<Exercise> getExerciseList(Query query);

    int countExerciseList(Query query);*/

    int deleteByExerciseId(Integer exerciseId);

    Exercise getExerciseByExerciseId(Integer exerciseId);

    int update(Exercise exercise);

    List<Exercise> getExerciseBySubjectId(@Param("query") Query query,@Param("subjectId") Integer subjectId);


    List<Exercise> getExerciseListBySubjectId(Integer subjectId);

    int deleteByUserId(Integer userId);

    List<EIdAndMId> getReviewExercise(Query query);

    int countReviewExercise(Query query);

    int updateById(@Param("id") Integer id,@Param("status") Integer status);

    List<Exercise> getReviewExerciseByUserId(@Param("userId") Integer userId,@Param("index") int index,@Param("size") Integer size, @Param("status") Integer status);

    int countReviewExerciseByUserId(@Param("userId") Integer userId,@Param("status") Integer status);

    void deleteBySubjectId(Integer subjectId);

    List<Integer> findExerciseIdListByUserId(Integer userId);

    void updateScore(@Param("score") Double score, @Param("exerciseId") Integer exerciseId);

    List<Exercise> getExerciseList(@Param("index") int index,@Param("size") Integer size,@Param("search") String search,@Param("type") String type,@Param("exerciseType") String exerciseType,
                                   @Param("order") String orderby,@Param("id") Integer subjectId,@Param("score") Integer score);

    int countExerciseList(@Param("search") String search,@Param("type") String type,@Param("exerciseType") String exerciseType,
                            @Param("order") String orderby,@Param("id") Integer subjectId,@Param("score") Integer score);
}
