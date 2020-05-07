package top.qiyoung.answer.controller.user;

import org.apache.commons.lang3.StringUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.qiyoung.answer.dto.ResultDTO;
import top.qiyoung.answer.exception.CustomizeErrorCode;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.model.UserList;
import top.qiyoung.answer.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/user/users")
public class UserUsersController {

    private static String user;
    @Resource
    private UserService userService;

    @RequestMapping("/information")
    @ResponseBody
    public MyUser information() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MyUser user = userService.getUserByUserDetails(userDetails);
        return user;
    }

    @RequestMapping("/userByUsername")
    @ResponseBody
    public MyUser userByUsername(String username) {
        MyUser user = userService.getUserByUsername(username);
        return user;
    }

    @RequestMapping("/personal/update")
    @ResponseBody
    public ResultDTO personalUpdate(@RequestParam(value = "userId")String userId,
                                    @RequestParam(value = "username")String username,
                                    @RequestParam(value = "phone")String phone,
                                    @RequestParam(value = "description")String description,
                                    @RequestParam(value = "nickname")String nickname,
                                    @RequestParam(value = "sex")String sex,
                                    @RequestParam(value = "avatarImgUrl")String avatarImgUrl,
                                    @RequestParam(value = "avatarImg", required = false) MultipartFile avatarImg) {
        MyUser myUser = new MyUser(Integer.parseInt(userId),nickname,username,null,null,description,avatarImgUrl,null,sex,phone);
        int result = userService.update(myUser, avatarImg);
        if (result == 2){
            return ResultDTO.errorOf(CustomizeErrorCode.IMG_UPLOAD_ERROR);

        }
        return ResultDTO.okOf();
    }

    @RequestMapping("/modifyPassword")
    public String modifyPassword(String oldPassword,String password,Model model,RedirectAttributes redirectAttributes){
        if (password.length()<6 || password == null){
            model.addAttribute("error","密码长度小于6，请重新尝试");
            return "user/modify-password";
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MyUser myUser = userService.getUserByUserDetails(userDetails);
        boolean checkpw = BCrypt.checkpw(oldPassword, myUser.getPassword());
        if (oldPassword == myUser.getPassword()){
            model.addAttribute("error","新密码不能与旧密码相同");
            return "user/modify-password";
        }
        if (checkpw){
            userService.modifyPassword(myUser,password);
        }else {
            model.addAttribute("error","旧密码错误，请重新尝试");
            return "user/modify-password";
        }
        redirectAttributes.addFlashAttribute("msg","密码修改成功");
        return "redirect:/index";
    }

    @RequestMapping("/count")
    @ResponseBody
    public ResultDTO count(Principal principal){
        List users = new ArrayList();
        boolean flag = false;
        if (StringUtils.isNotBlank(UserList.userList)) {
            String[] split = UserList.userList.split(",");
            for (String s : split) {
                if (s.equals(principal.getName())){
                    flag = true;
                }
                users.add(s);
            }
        }
        if (!flag){
            users.add(principal.getName());
        }
        String join = StringUtils.join(users, ",");
        UserList.userList = join;
        return ResultDTO.okOf();
    }

    @RequestMapping("/delCount")
    @ResponseBody
    public ResultDTO delCount(Principal principal){
        List users = new ArrayList();
        if (StringUtils.isNotBlank(UserList.userList)) {
            String[] split = UserList.userList.split(",");
            for (String s : split) {
                if (!principal.getName().equals(s)) {
                    users.add(s);
                }
            }
        }
        String join = StringUtils.join(users, ",");
        UserList.userList = join;
        return ResultDTO.okOf();
    }

    @RequestMapping("/getUser")
    @ResponseBody
    public MyUser getUser(Integer userId){
        return userService.getUserById(userId);
    }
}
