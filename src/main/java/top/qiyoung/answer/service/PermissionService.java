package top.qiyoung.answer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.dto.PaginationDTO;
import top.qiyoung.answer.enums.MessageStatusEnum;
import top.qiyoung.answer.enums.MessageTypeEnum;
import top.qiyoung.answer.enums.NotificationTypeEnum;
import top.qiyoung.answer.enums.UserRoleEnum;
import top.qiyoung.answer.exception.CustomizeErrorCode;
import top.qiyoung.answer.exception.CustomizeException;
import top.qiyoung.answer.mapper.NotificationMapper;
import top.qiyoung.answer.mapper.PermissionMapper;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.Message;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.model.Notification;
import top.qiyoung.answer.model.Query;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    public PaginationDTO<Message> getMessageList(Integer currentPage, Integer size, String order) {
        PaginationDTO<Message> paginationDTO = new PaginationDTO<>(currentPage,size);
        Query query = new Query();
        int count = permissionMapper.countMessageList();
        query.setSize(size);
        query.setIndex((currentPage-1)*size);
        List<Message> messages = permissionMapper.getMessageList(query);

        paginationDTO.setDataList(messages);
        paginationDTO.setTotalSize(count);
        paginationDTO.setTotalPage((int) Math.ceil((double) paginationDTO.getTotalSize() / (double) size));
        return paginationDTO;
    }

    public void updateRole(Integer messageId, Integer role, Integer userId, String reason,Integer status) {
        Message message = permissionMapper.getMessageByMessageId(messageId);
        Notification notification = new Notification(null,-1,userId,messageId, NotificationTypeEnum.NOTIFICATION_SYSTEM_PERMISSION.getType(),new Date(),0,message.getContent());
        notificationMapper.addNotification(notification);
        MyUser myUser = new MyUser();
        myUser.setUserId(userId);
        myUser.setRole(role);
        userMapper.update(myUser);
        permissionMapper.updateStatus(messageId,reason,status);
    }

    public int countPermission() {
        return permissionMapper.countAllPermission();
    }

    public Message getMessageByMessageId(Integer messageId) {
        Message message = permissionMapper.getMessageByMessageId(messageId);
        if (message == null){
            throw new CustomizeException(CustomizeErrorCode.MESSAGE_NOT_FOUND);
        }
        return message;
    }

    public PaginationDTO<Message> getMessageListByUserId(UserDetails userDetails, Integer currentPage, Integer size) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        Query query = new Query("create_time desc",(currentPage-1)*size,size);
        List<Message> messageList = permissionMapper.getMessageListByUserId(myUser.getUserId(), query);
        int count = permissionMapper.countMessageListByUserId(myUser.getUserId(), query);

        PaginationDTO<Message> dto = new PaginationDTO(currentPage,size,(int)Math.ceil((double)count/(double)size),count,null,null,messageList);
        return dto;
    }

    public void add(String content, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        Message message = new Message(null,null,content,1,null, myUser.getUserId(),new Date(),0);
        permissionMapper.add(message);
    }

    public void addNotice(String content, Integer role, String name) {
        MyUser myUser = userMapper.findUserByAccount(name);
        Message message;
        if (myUser.getRole() == UserRoleEnum.SUPER_MANAGER.getType()){
            message = new Message(null,null,content, MessageTypeEnum.NOTICE.getType(),null,myUser.getUserId(),new Date(), MessageStatusEnum.AUDIT_PASS.getType());
        }else {
            message = new Message(null,null,content, MessageTypeEnum.NOTICE.getType(),null,myUser.getUserId(),new Date(), MessageStatusEnum.AUDIT_PASS.getType());
        }
        permissionMapper.add(message);
        List<MyUser> general = userMapper.getUserByType(UserRoleEnum.GENERAL_USER.getType());
        List<MyUser> special = userMapper.getUserByType(UserRoleEnum.SPECIAL_USER.getType());
        if (role == null){
            for (MyUser user : general) {
                Notification notification = new Notification(null,-1,user.getUserId(),null,NotificationTypeEnum.NOTIFICATION_NOTICE.getType(),new Date(),0,content);
                notificationMapper.addNotification(notification);
            }
            for (MyUser user : special) {
                Notification notification = new Notification(null,-1,user.getUserId(),null,NotificationTypeEnum.NOTIFICATION_NOTICE.getType(),new Date(),0,content);
                notificationMapper.addNotification(notification);
            }
        }else if (role == 2){
            for (MyUser user : general) {
                Notification notification = new Notification(null,-1,user.getUserId(),null,NotificationTypeEnum.NOTIFICATION_NOTICE.getType(),new Date(),0,content);
                notificationMapper.addNotification(notification);
            }
        }else {
            for (MyUser user : special) {
                Notification notification = new Notification(null,-1,user.getUserId(),null,NotificationTypeEnum.NOTIFICATION_NOTICE.getType(),new Date(),0,content);
                notificationMapper.addNotification(notification);
            }
        }
    }
}
