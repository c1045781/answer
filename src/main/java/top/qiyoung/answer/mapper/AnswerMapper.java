package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import top.qiyoung.answer.model.Answer;

import java.util.List;

public interface AnswerMapper {
    void add(Answer answer);

    List<Answer> findAnswerByUserId(@Param("userId") Integer userId, @Param("currentPage") Integer currentPage,@Param("pageSize") Integer pageSize);

    int countAnswerByUserId(Integer userId);

    Answer findAnswerByAnswerId(Integer answerId);

    void update(Answer answer);

    List<Integer> findExerciseIdByUserId(@Param("userId") Integer userId,@Param("subjectId") Integer subjectId,@Param("search") String search);

    Answer findWrongAnswerByUserIdAndExerciseId(@Param("exerciseId") Integer id, @Param("userId") Integer userId);

    Answer findAnswerByExerciseIdAndUserId(@Param("exerciseId") Integer id, @Param("userId") Integer userId);

    void deleteByExerciseId(Integer exerciseId);

    void deleteByUserId(Integer userId);
}
