package top.qiyoung.answer.mapper;

import com.github.pagehelper.Page;
import top.qiyoung.answer.model.Query;
import top.qiyoung.answer.model.User;

public interface UserMapper {

    User login(User user);

    User findUserByAccount(String account);

    void insertUser(User user);

    Page<User> getUserList(Query query);

    Integer countUserList(Query query);

    void deleteById(User user);

    User getUserById(Integer id);

    void update(User user);
}
