package top.qiyoung.answer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.qiyoung.answer.mapper.ExerciseMapper;
import top.qiyoung.answer.mapper.ExerciseSetMapper;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.DTO.PaginationDTO;
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
    private UserMapper userMapper;
    @Resource
    private ExerciseMapper exerciseMapper;
    @Resource
    private ExerciseSetMapper exerciseSetMapper;

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
        if (avatarImg == null){
            upload = "/upload/default.jpg";
        }else {
            try {
                upload = fileUpload.upload(avatarImg);
            } catch (IOException e) {
                return 2;
            }
        }
        user.setCreateTime(new Date());
        user.setAvatarImgUrl(upload);
        userMapper.insertUser(user);
        return 1;
    }

    public PaginationDTO<User> getUserList(Integer currentPage, Integer size, String search, String type, String order, Integer role) {
        PaginationDTO<User> paginationDTO = new PaginationDTO<>(currentPage,size);
        Query query = new Query();
        query.setIndex((currentPage-1)*size);
        query.setSize(size);
        query.setRole(role);
        query.setSearch(search);
        query.setType(type);
        query.setOrder(order);
        List<User> userList = userMapper.getUserList(query);
        paginationDTO.setDataList(userList);

        query.setIndex(null);
        query.setSize(null);
        int count = userMapper.countUserList(query);
        paginationDTO.setTotalSize(count);
        paginationDTO.setTotalPage((int)Math.ceil((double) paginationDTO.getTotalSize()/(double)size));
        return paginationDTO;
    }

    public int deleteById(Integer id){
        exerciseMapper.deleteByUserId(id);
        exerciseSetMapper.deleteByUserId(id);
        int reslut = userMapper.deleteById(id);
        return reslut;
    }

    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    public int update(User user, MultipartFile avatarImg) {
        if (!avatarImg.isEmpty()) {
            User dbUser = userMapper.getUserById(user.getUserId());
            DeleteFile deleteFile = new DeleteFile();
            deleteFile.delFile(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\"+dbUser.getAvatarImgUrl());
            FileUpload fileUpload = new FileUpload();
            String upload = "/upload/default.jpg";
            try {
                upload = fileUpload.upload(avatarImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
            user.setAvatarImgUrl(upload);
        }
        return userMapper.update(user);
    }

    public int userCount() {
        Query query = new Query();
        return userMapper.countUserList(query);
    }
}
