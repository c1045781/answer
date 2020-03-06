package top.qiyoung.answer.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.User;

import javax.annotation.Resource;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Resource
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findUserByAccount(username);
        UserDetails details = org.springframework.security.core.userdetails.User.withUsername(user.getAccount()).password(user.getPassword()).authorities(user.getRole().toString()).build();
        return details;
    }
}
