package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.qiyoung.answer.model.Exercise;

import java.util.List;

@Component
public interface MidMapper {

    int insert(@Param("exerciseSetId") Integer exerciseSetId,@Param("exerciseId") Integer exerciseId);

    int deleteByExerciseSetId(Integer exerciseSetId);

    int deleteByExerciseId(Integer exerciseId);

    List<Integer> getExerciseIdListByExerciseSetId(Integer exerciseSetId);

    List<Exercise> getExerciseListByExerciseSetId(Integer exerciseSetId);

    List<Integer> getExerciseSetIdByExerciseId(Integer exerciseId);
}
