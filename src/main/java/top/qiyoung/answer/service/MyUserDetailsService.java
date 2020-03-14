package top.qiyoung.answer.service;

//import org.springframework.security.core.userdetails.MyUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.MyUser;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Resource
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = userMapper.findUserByAccount(username);
        UserDetails details = User.withUsername(myUser.getUsername()).password(myUser.getPassword()).authorities(myUser.getRole().toString()).build();
        return details;
    }
}
