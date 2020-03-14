package top.qiyoung.answer.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.mapper.PermissionMapper;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.Message;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.model.Query;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class PermissionService {

    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private UserMapper userMapper;

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
        return permissionMapper.getMessageByMessageId(messageId);
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
}
