package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import top.qiyoung.answer.model.Notification;

import java.util.List;

@Component
public interface NotificationMapper {

    List<Notification> getList(@Param("index") int i, @Param("pageSize") int size,@Param("userId") Integer userId,@Param("type") Integer type);

    int countList(@Param("userId") Integer userId,@Param("type") Integer type);

    Notification getNotificationByNotificationId(Integer notificationId);

    void addNotification(Notification notification);

    List<Integer> getNotifierIdByReceiverId(Integer userId);

    List<Integer> getReceiverIdByNotifierId(Integer userId);

    Notification getNewChat(@Param("secondUserId") Integer secondUserId,@Param("userId") Integer userId);

    List<Notification> getChat(@Param("userId") Integer userId,@Param("secondId") Integer secondId);

    List<Notification> getNewMessage(@Param("to") Integer userId,@Param("from") Integer userId1);

    int getNotificationNum(@Param("userId") Integer userId, @Param("type")int type,@Param("status") int status);

    void read(@Param("userId") Integer userId,@Param("type") Integer type);

    int getAllNotification(Integer userId);

    List<Notification> getSystemList(@Param("index") int i, @Param("pageSize") int size,@Param("userId") Integer userId, @Param("type") int type,
                                     @Param("type1") int type1,@Param("type2") int type2,@Param("type3") int type3);

    int countSystemList(@Param("userId") Integer userId, @Param("type") int type, @Param("type1") int type1,@Param("type2") int type2,@Param("type3") int type3);

    void delChat(@Param("userId") Integer userId,@Param("myUserId") Integer myUserId);

//    int getNotificationNumByChat(@Param("userId")Integer userId,@Param("type") int type,@Param("status") int status);
}
