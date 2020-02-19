package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MidMapper {

    int insert(@Param("exerciseSetId") Integer exerciseSetId,@Param("exerciseId") Integer exerciseId);

    int deleteByExerciseSetId(Integer exerciseSetId);

    int deleteByExerciseId(Integer exerciseId);

    List<Integer> getExerciseIdListByExerciseSetId(Integer exerciseSetId);
}
