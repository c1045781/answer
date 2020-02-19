package top.qiyoung.answer.mapper;

import top.qiyoung.answer.model.Comment;
import top.qiyoung.answer.model.Query;

import java.util.List;

public interface CommentMapper {
    List<Comment> getCommentList(Query query);

    int countCommentList(Query query);

    int deleteById(Integer id);

    List<Comment> getCommentListByExerciseId(Integer id);

    void addComment(Comment comment);
}
