package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import top.qiyoung.answer.model.Message;
import top.qiyoung.answer.model.Query;

import java.util.List;

public interface PermissionMapper {

    int countMessageList(Query query);

    List<Message> getMessageList(Query query);

    void updateStatus(@Param("id") Integer id,@Param("reason") String reason,@Param("status") Integer status);

    Message getMessageById(Integer id);

    List<Message> getMessageListByUserId(@Param("userId") Integer userId,@Param("query") Query query);

    void add(Message message);

    int countMessageListByUserId(@Param("userId") Integer userId,@Param("query") Query query);

    Message getMessageByExerciseIdAndUserId(@Param("exerciseId") Integer exerciseId,@Param("userId") Integer userId);
}
