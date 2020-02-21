package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import top.qiyoung.answer.model.Message;
import top.qiyoung.answer.model.Query;

import java.util.List;

public interface PermissionMapper {

    int countMessageList(Query query);

    List<Message> getMessageList(Query query);

    void updateStatus(Integer id);
}