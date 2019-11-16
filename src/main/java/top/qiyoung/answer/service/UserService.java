package top.qiyoung.answer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.Pagination;
import top.qiyoung.answer.model.Query;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.utils.DeleteFile;
import top.qiyoung.answer.utils.FileUpload;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    public User login(User user) {
        return userMapper.login(user);
    }

    public int insert(User user, MultipartFile avatarImg) {
        User dbuser = userMapper.findUserByAccount(user.getAccount());
        if (dbuser != null) {
            return 0;
        }
        FileUpload fileUpload = new FileUpload();
        String upload;
        try {
            upload = fileUpload.upload(avatarImg);
        } catch (IOException e) {
            return 2;
        }
        user.setCreateTime(new Date());
        user.setAvatarImgUrl(upload);
        userMapper.insertUser(user);
        return 1;
    }

    public Pagination<User> getUserList(Integer currentPage, Integer size, String search, String type, String order) {
        Pagination<User> pagination = new Pagination<>(currentPage,size);
        Query query = new Query();
        query.setIndex((currentPage-1)*size);
        query.setSize(size);
        query.setSearch(search);
        query.setType(type);
        query.setOrder(order);
        List<User> userList = userMapper.getUserList(query);
        pagination.setDataList(userList);

        query.setIndex(null);
        query.setSize(null);
        int count = userMapper.countUserList(query);
        pagination.setTotalSize(count);
        pagination.setTotalPage((int)Math.ceil((double)pagination.getTotalSize()/(double)size));
        return pagination;
    }

    public void deleteById(Integer id){
        userMapper.deleteById(id);
    }

    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    public void update(User user, MultipartFile avatarImg) {
        if (!avatarImg.isEmpty()) {
            User dbUser = userMapper.getUserById(user.getId());
            DeleteFile deleteFile = new DeleteFile();
            deleteFile.delFile(dbUser.getAvatarImgUrl());
            FileUpload fileUpload = new FileUpload();
            String upload = "/upload/default.jpg";
            try {
                upload = fileUpload.upload(avatarImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
            user.setAvatarImgUrl(upload);
        }
        userMapper.update(user);
    }

    public int userCount() {
        Query query = new Query();
        return userMapper.countUserList(query);
    }
}
