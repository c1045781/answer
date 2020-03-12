package top.qiyoung.answer.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.qiyoung.answer.mapper.*;
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
        if (avatarImg == null || "".equals(avatarImg.getOriginalFilename())){
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
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()));
        userMapper.insertUser(user);
        return 1;
    }

    public PaginationDTO<User> getUserList(Integer currentPage, Integer size, String search, String type, String order, Integer role,UserDetails userDetails) {
        User user = userMapper.findUserByAccount(userDetails.getUsername());
        PaginationDTO<User> paginationDTO = new PaginationDTO<>(currentPage,size);
        Query query = new Query();
        query.setIndex((currentPage-1)*size);
        query.setSize(size);
        query.setRole(role);
        query.setSearch(search);
        query.setType(type);
        query.setOrder(order);
        List<User> userList;
        int count;
        if (user.getRole() == 0){
            userList = userMapper.getUserList0(query);
            paginationDTO.setDataList(userList);
            count = userMapper.countUserList0(query);
        }else {
            userList = userMapper.getUserList1(query);
            paginationDTO.setDataList(userList);
            count = userMapper.countUserList1(query);
        }
        paginationDTO.setTotalSize(count);
        paginationDTO.setTotalPage((int)Math.ceil((double) paginationDTO.getTotalSize()/(double)size));
        return paginationDTO;
    }

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

    public User getUserById(UserDetails userDetails) {
        User user = userMapper.findUserByAccount(userDetails.getUsername());
        return userMapper.getUserById(user.getUserId());
    }

    public int update(User user, MultipartFile avatarImg) {
        if (!avatarImg.isEmpty()) {
            User dbUser = userMapper.getUserById(user.getUserId());
            DeleteFile deleteFile = new DeleteFile();
            if (!dbUser.getAvatarImgUrl().equals("/upload/default.jpg"))
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
        if (user.getPassword() != null)
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()));
        return userMapper.update(user);
    }

    public int userCount() {
        Query query = new Query();
        return userMapper.countUserList1(query);
    }

    public User getUserByUserId(Integer userId) {
        return userMapper.getUserById(userId);
    }

    public User getUserByUserDetails(UserDetails userDetails) {
        User user = userMapper.findUserByAccount(userDetails.getUsername());
        return user;
    }

    public void modifyPassword(UserDetails principal, String password) {
        User user = userMapper.findUserByAccount(principal.getUsername());
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPassword(password);
        userMapper.modifyPassword(user);
    }
}
