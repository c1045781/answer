package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
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

    List<ExerciseSet> getExerciseSetListByUserId(@Param("index") Integer index,@Param("pageSize") Integer size,@Param("orderBy") String orderBy,@Param("userId") Integer userId);

    int countExerciseSetListByUserId(Integer userId);

    void deleteBySubjectId(Integer subjectId);

    List<Integer> findExerciseSetIdListByUser(Integer userId);

    void addLike(Integer exerciseSetId);

    void delLike(Integer exerciseSetId);

    List<ExerciseSet> getHighLikeExerciseSet(Integer subject);

    void delExerciseCountById(Integer id);
}
