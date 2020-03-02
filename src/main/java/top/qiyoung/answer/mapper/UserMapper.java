package top.qiyoung.answer.mapper;

import com.github.pagehelper.Page;
import top.qiyoung.answer.model.Query;
import top.qiyoung.answer.model.User;

import java.util.List;

public interface UserMapper {

    User login(User user);

    User findUserByAccount(String account);

    void insertUser(User user);

    int deleteById(Integer id);

    User getUserById(Integer id);

    int update(User user);

    List<User> getUserByUsername(String username);

    List<User> getUserList0(Query query);

    int countUserList0(Query query);

    List<User> getUserList1(Query query);

    int countUserList1(Query query);
}
