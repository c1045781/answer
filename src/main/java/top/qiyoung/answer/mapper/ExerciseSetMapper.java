package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import top.qiyoung.answer.model.ExerciseSet;
import top.qiyoung.answer.model.Query;

import java.util.List;

public interface ExerciseSetMapper {
    int insert(ExerciseSet exerciseSet);

//    List<ExerciseSet> getAll();

    List<ExerciseSet> getExerciseSetList(@Param("query") Query query, @Param("title") String title,
                                         @Param("subjectId") Integer subjectId,  @Param("userId") Integer userId);

    int countExerciseSetList(@Param("query") Query query, @Param("title") String title,
                             @Param("subjectId") Integer subjectId,  @Param("userId") Integer userId);

    void delete(Integer exerciseSetId);

    Integer getLastId();

    ExerciseSet getExerciseSetById(Integer exerciseSetId);

    void update(ExerciseSet exerciseSet);
}
