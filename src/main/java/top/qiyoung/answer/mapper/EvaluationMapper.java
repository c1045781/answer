package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.qiyoung.answer.model.Evaluation;

@Component
public interface EvaluationMapper {

    Evaluation getEvaluationByUserIdAndExerciseId(@Param("userId") Integer userId,@Param("exerciseId") Integer exerciseId);

    void update(Evaluation evaluation);

    void insert(Evaluation evaluation);

    Integer countByExerciseId(Integer exerciseId);

    Double findAVGScoreByExerciseId(Integer exerciseId);

    void deleteByExerciseId(Integer exerciseId);

    void deleteByUserId(Integer userId);
}
