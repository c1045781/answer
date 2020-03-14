package top.qiyoung.answer.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Date;


public class MyUser{
    private Integer userId;
//    private String account;
    private String nickname;
    private String username;
    private String password;
    private Integer role;
    private String description;
    private String avatarImgUrl;
    private Date createTime;
    private String sex;
    private String phone;

    /*public MyUser(Integer userId, String nickname, String username, String password,Integer role, Collection<? extends GrantedAuthority> authority, String description, String avatarImgUrl, Date createTime, String sex, String phone) {
        super(username,password,authority);
        this.userId = userId;
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.role = role;
        this.description = description;
        this.avatarImgUrl = avatarImgUrl;
        this.createTime = createTime;
        this.sex = sex;
        this.phone = phone;
    }

    public MyUser(String username, String password , Collection<? extends GrantedAuthority> authority) {
        super(username,password,authority);
        this.username = username;
        this.password = password;
    }*/

    /*public MyUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public MyUser(String username, String password, Collection<? extends GrantedAuthority> authorities,Integer userId,String nickname,Integer role,String description,
                  String avatarImgUrl,Date createTime,String sex,String phone) {
        super(username, password, authorities);
        this.userId = userId;
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.role = role;
        this.description = description;
        this.avatarImgUrl = avatarImgUrl;
        this.createTime = createTime;
        this.sex = sex;
        this.phone = phone;

    }

    public MyUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

    }*/

    public MyUser(Integer userId, String nickname, String username, String password, Integer role, String description, String avatarImgUrl, Date createTime, String sex, String phone) {
        this.userId = userId;
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.role = role;
        this.description = description;
        this.avatarImgUrl = avatarImgUrl;
        this.createTime = createTime;
        this.sex = sex;
        this.phone = phone;
    }

    public MyUser() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatarImgUrl() {
        return avatarImgUrl;
    }

    public void setAvatarImgUrl(String avatarImgUrl) {
        this.avatarImgUrl = avatarImgUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
