package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.qiyoung.answer.enums.UserRoleEnum;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.model.Query;

import java.util.List;

@Component
public interface UserMapper {

    MyUser login(MyUser myUser);

    MyUser findUserByAccount(String account);

    void insertUser(MyUser myUser);

    int deleteById(Integer id);

    MyUser getUserById(Integer id);

    int update(MyUser myUser);

    List<MyUser> getUserByNickname(String username);

    List<MyUser> getUserList0(Query query);

    int countUserList0(Query query);

    List<MyUser> getUserList1(Query query);

    int countUserList1(Query query);

    void modifyPassword(MyUser myUser);

    List<MyUser> getUserByType(Integer role);
}
