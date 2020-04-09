package top.qiyoung.answer.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.dto.NotificationDTO;
import top.qiyoung.answer.dto.PaginationDTO;
import top.qiyoung.answer.enums.MessageTypeEnum;
import top.qiyoung.answer.enums.NotificationStatusEnum;
import top.qiyoung.answer.enums.NotificationTypeEnum;
import top.qiyoung.answer.enums.UserRoleEnum;
import top.qiyoung.answer.mapper.NotificationMapper;
import top.qiyoung.answer.mapper.PermissionMapper;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.Message;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.model.Notification;
import top.qiyoung.answer.model.UserList;
import top.qiyoung.answer.utils.NotificationByCreateTime;

import java.security.Principal;
import java.util.*;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    public PaginationDTO<NotificationDTO> getList(Integer currentPage, int size, UserDetails userDetails, Integer type) {
        MyUser user = userMapper.findUserByAccount(userDetails.getUsername());
        List<Notification> notifications = notificationMapper.getList((currentPage-1)*size,size, user.getUserId(),type);
        notificationMapper.read(user.getUserId(),type);
        int count = notificationMapper.countList(user.getUserId(),type);
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            MyUser notifier = userMapper.getUserById(notification.getNotifierId());
            MyUser receiver = userMapper.getUserById(notification.getReceiverId());
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setNotifier(notifier);
            notificationDTO.setReceiver(receiver);
            if (notification.getType() ==3 || notification.getType() ==4){
                Message message = permissionMapper.getMessageByMessageId(notification.getOuterId());
                notificationDTO.setMessage(message);
            }
            notificationDTOS.add(notificationDTO);
        }
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO(currentPage,size,(int)Math.ceil((double)count/(double)size),count,null,null,notificationDTOS);
        return paginationDTO;
    }

    public Notification getNotificationByNotificationId(Integer notificationId) {
        return notificationMapper.getNotificationByNotificationId(notificationId);
    }

    public void addNotification(String from ,String to, String content) {
        String users = UserList.userList;
        String[] split = users.split(",");
        boolean flag = false;
        for (String s : split) {
            if (to.equals(s)){
                flag = true;
            }
        }
        MyUser fromUser = userMapper.findUserByAccount(from);
        MyUser toUser = userMapper.findUserByAccount(to);
        Notification notification = new Notification(null,fromUser.getUserId(),toUser.getUserId(),null,5,new Date(),null,content);
        if (flag){
            notification.setStatus(NotificationStatusEnum.READ.getType());
        }else {
            notification.setStatus(NotificationStatusEnum.UNREAD.getType());
        }
        notificationMapper.addNotification(notification);
    }

    public Set<NotificationDTO> chat(Principal principal) {
        MyUser user = userMapper.findUserByAccount(principal.getName());
        List<Integer> notiferIds = notificationMapper.getNotifierIdByReceiverId(user.getUserId());
        List<Integer> receiverIds = notificationMapper.getReceiverIdByNotifierId(user.getUserId());
        Set<Integer> set = new HashSet<>();
        for (Integer notiferId : notiferIds) {
            set.add(notiferId);
        }
        for (Integer receiverId : receiverIds) {
            set.add(receiverId);
        }
        Set<NotificationDTO> notificationDTOS = new TreeSet<>(new NotificationByCreateTime());
        for (Integer integer : set) {
            Notification notification = notificationMapper.getNewChat(integer,user.getUserId());
            NotificationDTO notificationDTO = new NotificationDTO();

            BeanUtils.copyProperties(notification,notificationDTO);
            if (notification.getNotifierId() == user.getUserId()){
                MyUser secondUser = userMapper.getUserById(notification.getReceiverId());
                notificationDTO.setNotifier(user);
                notificationDTO.setReceiver(secondUser);
            }else {
                MyUser secondUser = userMapper.getUserById(notification.getNotifierId());
                notificationDTO.setNotifier(secondUser);
                notificationDTO.setReceiver(user);
            }
            notificationDTOS.add(notificationDTO);
        }
        notificationMapper.read(user.getUserId(),NotificationTypeEnum.NOTIFICATION_CHAT.getType());
        return notificationDTOS;
    }

    public List<NotificationDTO> showChat(Principal principal,Integer secondId) {
        MyUser user = userMapper.findUserByAccount(principal.getName());
        List<Notification> notifications = notificationMapper.getChat(user.getUserId(),secondId);
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            MyUser notifier = userMapper.getUserById(notification.getNotifierId());
            MyUser receiver = userMapper.getUserById(notification.getReceiverId());
            notificationDTO.setReceiver(receiver);
            notificationDTO.setNotifier(notifier);
            notificationDTOS.add(notificationDTO);
        }
        return notificationDTOS;
    }

    public List<NotificationDTO> newMessage(String to, String from) {
        MyUser toUser = userMapper.findUserByAccount(to);
        MyUser fromUser = userMapper.findUserByAccount(from);
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        List<Notification> notifications = notificationMapper.getNewMessage(toUser.getUserId(),fromUser.getUserId());
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            MyUser receiver = userMapper.getUserById(notification.getReceiverId());
            MyUser notifier = userMapper.getUserById(notification.getNotifierId());
            notificationDTO.setNotifier(notifier);
            notificationDTO.setReceiver(receiver);
            notificationDTOS.add(notificationDTO);
        }
        return notificationDTOS;
    }

    public  Map<String,Integer> getNotificationNum(String name) {
        MyUser user = userMapper.findUserByAccount(name);
        int systemNum4 = 0;
        if (user.getRole() == UserRoleEnum.GENERAL_USER.getType()){
            systemNum4 = notificationMapper.getNotificationNum(user.getUserId(), NotificationTypeEnum.NOTIFICATION_NOTICE_GENERAL.getType(),NotificationStatusEnum.UNREAD.getType());
        }else if (user.getRole() == UserRoleEnum.SPECIAL_USER.getType()){
            systemNum4 = notificationMapper.getNotificationNum(user.getUserId(), NotificationTypeEnum.NOTIFICATION_NOTICE_SPECIAL.getType(),NotificationStatusEnum.UNREAD.getType());
        }
        int replyNum = notificationMapper.getNotificationNum(user.getUserId(), NotificationTypeEnum.NOTIFICATION_COMMENT.getType(),NotificationStatusEnum.UNREAD.getType());
        int likeNum = notificationMapper.getNotificationNum(user.getUserId(), NotificationTypeEnum.NOTIFICATION_LIKE.getType(),NotificationStatusEnum.UNREAD.getType());
        int systemNum1 = notificationMapper.getNotificationNum(user.getUserId(), NotificationTypeEnum.NOTIFICATION_SYSTEM_EXERCISE.getType(),NotificationStatusEnum.UNREAD.getType());
        int systemNum2 = notificationMapper.getNotificationNum(user.getUserId(), NotificationTypeEnum.NOTIFICATION_SYSTEM_PERMISSION.getType(),NotificationStatusEnum.UNREAD.getType());
        int systemNum3 = notificationMapper.getNotificationNum(user.getUserId(), NotificationTypeEnum.NOTIFICATION_NOTICE.getType(),NotificationStatusEnum.UNREAD.getType());
        int chatNum = notificationMapper.getNotificationNum(user.getUserId(), NotificationTypeEnum.NOTIFICATION_CHAT.getType(),NotificationStatusEnum.UNREAD.getType());
        Map<String,Integer> map = new HashMap();
        map.put("replyNum",replyNum);
        map.put("likeNum",likeNum);
        map.put("systemNum",systemNum1+systemNum2+systemNum3+systemNum4);
        map.put("chatNum",chatNum);
        return map;
    }

    public Integer getAllNotification(String name) {
        MyUser user = userMapper.findUserByAccount(name);
        int count = notificationMapper.getAllNotification(user.getUserId());
        return count;
    }

    public PaginationDTO<NotificationDTO> getSystemList(Integer currentPage, int size, String name) {
        MyUser user = userMapper.findUserByAccount(name);
        List<Notification> notifications;
        int count;
        if (user.getRole() == UserRoleEnum.GENERAL_USER.getType()){
            notifications = notificationMapper.getSystemList((currentPage-1)*size,size, user.getUserId(),NotificationTypeEnum.NOTIFICATION_NOTICE.getType(),
                    NotificationTypeEnum.NOTIFICATION_NOTICE_GENERAL.getType(),NotificationTypeEnum.NOTIFICATION_SYSTEM_PERMISSION.getType(),
                    NotificationTypeEnum.NOTIFICATION_SYSTEM_EXERCISE.getType());
            count = notificationMapper.countSystemList(user.getUserId(),NotificationTypeEnum.NOTIFICATION_NOTICE.getType(),
                    NotificationTypeEnum.NOTIFICATION_NOTICE_GENERAL.getType(),NotificationTypeEnum.NOTIFICATION_SYSTEM_PERMISSION.getType(),
                    NotificationTypeEnum.NOTIFICATION_SYSTEM_EXERCISE.getType());
            notificationMapper.read(user.getUserId(),NotificationTypeEnum.NOTIFICATION_NOTICE_GENERAL.getType());
        }else {
            notifications = notificationMapper.getSystemList((currentPage-1)*size,size, user.getUserId(),NotificationTypeEnum.NOTIFICATION_NOTICE.getType(),
                    NotificationTypeEnum.NOTIFICATION_NOTICE_SPECIAL.getType(),NotificationTypeEnum.NOTIFICATION_SYSTEM_PERMISSION.getType(),
                    NotificationTypeEnum.NOTIFICATION_SYSTEM_EXERCISE.getType());
            count = notificationMapper.countSystemList(user.getUserId(),NotificationTypeEnum.NOTIFICATION_NOTICE.getType(),
                    NotificationTypeEnum.NOTIFICATION_NOTICE_SPECIAL.getType(),NotificationTypeEnum.NOTIFICATION_SYSTEM_PERMISSION.getType(),
                    NotificationTypeEnum.NOTIFICATION_SYSTEM_EXERCISE.getType());
            notificationMapper.read(user.getUserId(),NotificationTypeEnum.NOTIFICATION_NOTICE_SPECIAL.getType());
        }
        notificationMapper.read(user.getUserId(),NotificationTypeEnum.NOTIFICATION_NOTICE.getType());
        notificationMapper.read(user.getUserId(),NotificationTypeEnum.NOTIFICATION_SYSTEM_PERMISSION.getType());
        notificationMapper.read(user.getUserId(),NotificationTypeEnum.NOTIFICATION_SYSTEM_EXERCISE.getType());

        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            MyUser notifier = userMapper.getUserById(notification.getNotifierId());
            MyUser receiver = userMapper.getUserById(notification.getReceiverId());
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setNotifier(notifier);
            notificationDTO.setReceiver(receiver);
            if (notification.getType() ==3 || notification.getType() ==4){
                Message message = permissionMapper.getMessageByMessageId(notification.getOuterId());
                notificationDTO.setMessage(message);
            }
            notificationDTOS.add(notificationDTO);
        }
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO(currentPage,size,(int)Math.ceil((double)count/(double)size),count,null,null,notificationDTOS);
        return paginationDTO;
    }

    public void delChat(String username,String myUsername) {
        MyUser user = userMapper.findUserByAccount(username);
        MyUser myUser = userMapper.findUserByAccount(myUsername);
        notificationMapper.delChat(user.getUserId(),myUser.getUserId());
    }

    public void update(Integer messageId,Integer userId, String reason, Integer status) {
        permissionMapper.updateStatus(messageId,reason,status);
        if (status == 1){
            Message message = permissionMapper.getMessageByMessageId(messageId);
            List<MyUser> general = userMapper.getUserByType(UserRoleEnum.GENERAL_USER.getType());
            List<MyUser> special = userMapper.getUserByType(UserRoleEnum.SPECIAL_USER.getType());
            if (message.getType() == MessageTypeEnum.NOTICE.getType()) {
                for (MyUser user : general) {
                    Notification notification = new Notification(null,userId,user.getUserId(),null,NotificationTypeEnum.NOTIFICATION_NOTICE.getType(),new Date(),0,message.getContent());
                    notificationMapper.addNotification(notification);
                }
                for (MyUser user : special) {
                    Notification notification = new Notification(null,userId,user.getUserId(),null,NotificationTypeEnum.NOTIFICATION_NOTICE.getType(),new Date(),0,message.getContent());
                    notificationMapper.addNotification(notification);
                }
            }else if (message.getType() == MessageTypeEnum.NOTICE_GENERAL.getType()) {
                for (MyUser user : general) {
                    Notification notification = new Notification(null,userId,user.getUserId(),null,NotificationTypeEnum.NOTIFICATION_NOTICE.getType(),new Date(),0,message.getContent());
                    notificationMapper.addNotification(notification);
                }
            }else if (message.getType() == MessageTypeEnum.NOTICE_SPECIAL.getType()) {
                for (MyUser user : special) {
                    Notification notification = new Notification(null,userId,user.getUserId(),null,NotificationTypeEnum.NOTIFICATION_NOTICE.getType(),new Date(),0,message.getContent());
                    notificationMapper.addNotification(notification);
                }
            }
        }
    }
}
