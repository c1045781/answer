package top.qiyoung.answer.mapper;

import top.qiyoung.answer.model.Comment;
import top.qiyoung.answer.model.Query;

import java.util.List;

public interface CommentMapper {
    List<Comment> getCommentList(Query query);

    int countCommentList(Query query);

    int deleteByCommentId(Integer commentId);

    List<Comment> getCommentListByExerciseId(Integer exerciseId);

    void addComment(Comment comment);

    void deleteByExerciseId(Integer exerciseId);

    void deleteByUserId(Integer userId);

    int countByExerciseId(Integer exerciseId);

    Comment getCommentByCommentId(Integer commentId);
}
