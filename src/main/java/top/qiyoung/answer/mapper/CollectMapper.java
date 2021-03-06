package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.qiyoung.answer.model.Collect;

import java.util.List;

@Component
public interface CollectMapper {
    void addCollect(Collect collect);

    void deleteCollect(@Param("exerciseId") Integer exerciseId,@Param("userId") Integer userId);

    Collect findCollectByExerciseIdAndUserId(@Param("exerciseId") Integer exerciseId,@Param("userId") Integer userId);

    List<Collect> findCollectByUserId(@Param("userId") Integer userId,@Param("index") Integer index,@Param("size") Integer pageSize);

    int countCollectByUserId(Integer userId);

    void deleteCollectByExerciseId(Integer exerciseId);

    void deleteByUserId(Integer userId);
}
