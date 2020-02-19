package top.qiyoung.answer.service;

import org.springframework.stereotype.Service;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.mapper.PermissionMapper;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.Message;
import top.qiyoung.answer.model.Query;
import top.qiyoung.answer.model.User;

import javax.annotation.Resource;
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
        int count = permissionMapper.countMessageList(query);
        query.setSize(size);
        query.setIndex((currentPage-1)*size);
        List<Message> messages = permissionMapper.getMessageList(query);

        paginationDTO.setDataList(messages);
        paginationDTO.setTotalSize(count);
        paginationDTO.setTotalPage((int) Math.ceil((double) paginationDTO.getTotalSize() / (double) size));
        return paginationDTO;
    }

    public void updateRole(Integer id, Integer role,Integer userId) {
        User user = new User();
        user.setUserId(userId);
        user.setRole(role);
        userMapper.update(user);
        permissionMapper.updateStatus(id);
    }

    public int countPermission() {
        return permissionMapper.countMessageList(new Query());
    }
}
