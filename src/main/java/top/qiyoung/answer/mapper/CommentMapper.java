package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.qiyoung.answer.model.Comment;
import top.qiyoung.answer.model.Query;

import java.util.List;

@Component
public interface CommentMapper {
    List<Comment> getCommentList(@Param("index") Integer index,@Param("size") Integer size,@Param("exerciseId") String exerciseId
            ,@Param("userId") String userId,@Param("order") String order);

    int countCommentList(@Param("exerciseId") String exerciseId ,@Param("userId") String userId);

    int deleteByCommentId(Integer commentId);

    List<Comment> getCommentListByParentId(@Param("parentId") Integer parentId,@Param("index") Integer index,@Param("size") Integer size,@Param("type")Integer type,@Param("order")String order);

    void addComment(Comment comment);

    void deleteByExerciseId(Integer exerciseId);

    void deleteByUserId(Integer userId);

    int countByExerciseId(Integer exerciseId);

    Comment getCommentByCommentId(Integer commentId);

    void addLike(Integer commentId);

    void delLike(Integer commentId);

    int countCommentListByParentId(@Param("parentId") Integer parentId,@Param("type")Integer type);

    /*List<Comment> getSecondCommentList(@Param("commentId") Integer commentId,@Param("index") Integer index,@Param("size") int size);

    Integer countSecondCommentList(Integer commentId);*/
}
