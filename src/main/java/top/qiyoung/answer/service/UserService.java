package top.qiyoung.answer.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.qiyoung.answer.exception.CustomizeErrorCode;
import top.qiyoung.answer.exception.CustomizeException;
import top.qiyoung.answer.mapper.*;
import top.qiyoung.answer.dto.PaginationDTO;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.model.Query;
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
    private ExerciseService exerciseService;
    @Resource
    private ExerciseSetService exerciseSetService;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private EvaluationMapper evaluationMapper;
    @Resource
    private AnswerMapper answerMapper;
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private NoteMapper noteMapper;

    public MyUser login(MyUser myUser) {
        return userMapper.login(myUser);
    }

    public int insert(MyUser myUser, MultipartFile avatarImg) {
        MyUser dbuser = userMapper.findUserByAccount(myUser.getUsername());
        if (dbuser != null) {
            return 0;
        }
        FileUpload fileUpload = new FileUpload();
        String upload;
        if (avatarImg == null || "".equals(avatarImg.getOriginalFilename())){
            upload = "/upload/default.jpg";
        }else {
            try {
                upload = fileUpload.uploadImg(avatarImg);
            } catch (IOException e) {
                return 2;
            }
        }
        myUser.setCreateTime(new Date());
        myUser.setAvatarImgUrl(upload);
        myUser.setPassword(BCrypt.hashpw(myUser.getPassword(),BCrypt.gensalt()));
        userMapper.insertUser(myUser);
        return 1;
    }

    public PaginationDTO<MyUser> getUserList(Integer currentPage, Integer size, String search, String type, String order, Integer role, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        PaginationDTO<MyUser> paginationDTO = new PaginationDTO<>(currentPage,size);
        Query query = new Query();
        query.setIndex((currentPage-1)*size);
        query.setSize(size);
        query.setRole(role);
        query.setSearch(search);
        query.setType(type);
        query.setOrder(order);
        List<MyUser> myUserList;
        int count;
        if (myUser.getRole() == 0){
            myUserList = userMapper.getUserList0(query);
            paginationDTO.setDataList(myUserList);
            count = userMapper.countUserList0(query);
        }else {
            myUserList = userMapper.getUserList1(query);
            paginationDTO.setDataList(myUserList);
            count = userMapper.countUserList1(query);
        }
        paginationDTO.setTotalSize(count);
        paginationDTO.setTotalPage((int)Math.ceil((double) paginationDTO.getTotalSize()/(double)size));
        return paginationDTO;
    }

    @Transactional
    public void deleteById(Integer userId){
        answerMapper.deleteByUserId(userId);
        collectMapper.deleteByUserId(userId);
        commentMapper.deleteByUserId(userId);
        evaluationMapper.deleteByUserId(userId);
        permissionMapper.deleteByUserId(userId);
        noteMapper.deleteByUserId(userId);


        exerciseService.deleteByUserId(userId);
        exerciseSetService.deleteByUserId(userId);

        userMapper.deleteById(userId);
    }

    public MyUser getUserById(UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        MyUser user = userMapper.getUserById(myUser.getUserId());
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    public int update(MyUser myUser, MultipartFile avatarImg) {
        if (!avatarImg.isEmpty()) {
            MyUser dbMyUser = userMapper.getUserById(myUser.getUserId());
            if (dbMyUser == null){
                throw new CustomizeException(CustomizeErrorCode.USER_NOT_FOUND);
            }
            DeleteFile deleteFile = new DeleteFile();
            if (!dbMyUser.getAvatarImgUrl().equals("/upload/default.jpg"))
            deleteFile.delFile(System.getProperty("myUser.dir") + "\\src\\main\\resources\\static\\"+ dbMyUser.getAvatarImgUrl());
            FileUpload fileUpload = new FileUpload();
            String upload;
            if (avatarImg == null || "".equals(avatarImg.getOriginalFilename())){
                upload = "/upload/default.jpg";
            }else {
                try {
                    upload = fileUpload.uploadImg(avatarImg);
                } catch (IOException e) {
                    return 2;
                }
            }
            myUser.setAvatarImgUrl(upload);
        }
        if (myUser.getPassword() != null)
        myUser.setPassword(BCrypt.hashpw(myUser.getPassword(),BCrypt.gensalt()));
        userMapper.update(myUser);
        return 1;
    }

    public int userCount() {
        Query query = new Query();
        return userMapper.countUserList1(query);
    }

    public MyUser getUserByUserId(Integer userId) {
        MyUser user = userMapper.getUserById(userId);
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    public MyUser getUserByUserDetails(UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        if (myUser == null){
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_FOUND);
        }
        return myUser;
    }

    public void modifyPassword(MyUser myUser, String password) {
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        myUser.setPassword(password);
        userMapper.modifyPassword(myUser);
    }
}
