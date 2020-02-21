package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import top.qiyoung.answer.model.Collect;

public interface CollectMapper {
    void addCollect(@Param("exerciseId") Integer exerciseId,@Param("userId") Integer userId);

    void deleteCollect(@Param("exerciseId") Integer exerciseId,@Param("userId") Integer userId);

    Collect findCollectByExerciseIdAndUserId(@Param("exerciseId") Integer exerciseId,@Param("userId") Integer userId);
}
