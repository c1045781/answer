package top.qiyoung.answer.mapper;

import com.github.pagehelper.Page;
import top.qiyoung.answer.model.Query;
import top.qiyoung.answer.model.User;

import java.util.List;

public interface UserMapper {

    User login(User user);

    User findUserByAccount(String account);

    void insertUser(User user);

    Page<User> getUserList(Query query);

    Integer countUserList(Query query);

    int deleteById(Integer id);

    User getUserById(Integer id);

    int update(User user);

    List<User> getUserByUsername(String username);

}
