package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.qiyoung.answer.model.Comment;
import top.qiyoung.answer.model.Query;

import java.util.List;

@Component
public interface CommentMapper {
    List<Comment> getCommentList(Query query);

    int countCommentList(Query query);

    int deleteByCommentId(Integer commentId);

    List<Comment> getCommentListByParentIdType1(@Param("parentId") Integer parentId,@Param("index") Integer index,@Param("size") Integer size);

    List<Comment> getCommentListByParentIdType2(@Param("parentId") Integer parentId,@Param("index") Integer index,@Param("size") Integer size);

    void addComment(Comment comment);

    void deleteByExerciseId(Integer exerciseId);

    void deleteByUserId(Integer userId);

    int countByExerciseId(Integer exerciseId);

    Comment getCommentByCommentId(Integer commentId);

    void addLike(Integer commentId);

    void delLike(Integer commentId);

    int CountCommentListByParentIdType1(Integer parentId);

    int CountCommentListByParentIdType2(Integer parentId);
}
