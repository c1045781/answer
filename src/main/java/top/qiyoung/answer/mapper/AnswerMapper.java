package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import top.qiyoung.answer.model.Answer;

import java.util.List;

public interface AnswerMapper {
    void add(Answer answer);

    List<Answer> findAnswerByUserId(@Param("userId") Integer userId, @Param("currentPage") Integer currentPage,@Param("pageSize") Integer pageSize);

    int countAnswerByUserId(Integer userId);

    Answer findAnswerByExerciseIdAndUserId(@Param("exerciseId") Integer exerciseId,@Param("userId") Integer userId);

    void update(Answer answer);
}
