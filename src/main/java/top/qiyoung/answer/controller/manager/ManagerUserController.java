package top.qiyoung.answer.controller.manager;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/manager/user")
public class ManagerUserController {

    @Resource
    private UserService userService;

    // 查询用户
    @RequestMapping("/check")
    public String checkUser(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                            @RequestParam(value = "size", defaultValue = "10") Integer size,
                            @RequestParam(value = "search", required = false) String search,
                            @RequestParam(value = "type", required = false) String type,
                            @RequestParam(value = "role", required = false ) Integer role,
                            @RequestParam(value = "order", defaultValue = "user_id asc") String order,
                            Model model,
                            HttpServletRequest request) {
//        MyUser myUser = (MyUser) request.getSession().getAttribute("myUser");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PaginationDTO paginationDTO = userService.getUserList(currentPage, size, search, type, order,role, userDetails);
        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("search", search);
        model.addAttribute("type", type);
        model.addAttribute("role", role);
        return "manage/user/user";
    }

    // 跳转添加用户页面
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "manage/user/add-user";
    }

    // 添加或更新用户
    @RequestMapping("/addOrUpdate")
    public String addOrUpdate(MyUser myUser, Model model,
                              @RequestParam(value = "avatarImg", required = false) MultipartFile avatarImg){

        String filename = avatarImg.getOriginalFilename();
        String[] split = filename.split("\\.");
        String suffix = split[split.length - 1];
        if (!avatarImg.isEmpty() && !(suffix.equals("jpg") || suffix.equals("png") || suffix.equals("jpeg"))) {
            model.addAttribute("message", "图片格式错误");
            model.addAttribute("user", myUser);
            return "manage/user/add-user";
        }
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (myUser != null){
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(myUser.getPhone());
            if (!m.matches()){
                model.addAttribute("message", "手机号格式错误");
                model.addAttribute("user", myUser);
                return "manage/user/add-user";
            }
        }

        if (myUser.getUserId() == null) {
            int insert = userService.insert(myUser, avatarImg);
            if (insert == 0) {
                model.addAttribute("message", "添加失败,账号已存在");
                return "manage/user/add-user";
            } else if (insert == 2) {
                model.addAttribute("message", "图片上传失败请重新上传");
                return "manage/user/add-user";
            } else {
                return "redirect:/manager/user/check";
            }
        } else {
            int result = userService.update(myUser, avatarImg);
            if (result <= 0){
                MyUser dbuser = userService.getUserByUserId(myUser.getUserId());
                model.addAttribute("user", dbuser);
                return "manage/user/add-user";
            }
            return "redirect:/manager/user/check";
        }
    }

    // 跳转更新页面
    @RequestMapping("/toUpdate")
    public String toUpdate(Integer userId, Model model) {
        MyUser myUser = userService.getUserByUserId(userId);
        model.addAttribute("user", myUser);
        return "manage/user/add-user";
    }

    // 跳转密码更新页面
    @RequestMapping("/toModifyPassword")
    public String toModifyPassword() {
        return "manage/user/modify-password";
    }

    // 删除用户
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(Integer userId) {
        userService.deleteById(userId);
        return "success";
    }

    @RequestMapping("/info")
    public String manager(HttpServletRequest request,Model model){
//        MyUser myUser = (MyUser) request.getSession().getAttribute("myUser");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MyUser dbMyUser = userService.getUserById(userDetails);
        model.addAttribute("myUser", dbMyUser);
        return "/manage/manager-info";
    }


    @RequestMapping("/modifyPassword")
    public String modifyPassword(String oldPassword,String password,Model model){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MyUser myUser = userService.getUserByUserDetails(userDetails);
        boolean checkpw = BCrypt.checkpw(oldPassword, myUser.getPassword());
        if (checkpw){
            userService.modifyPassword(myUser,password);
        }else {
            model.addAttribute("error","旧密码错误，请重试");
            return "manage/user/modify-password";
        }

        if (password.length()<6 || password == null){
            model.addAttribute("error","密码长度小于6，请重试");
            return "manage/user/modify-password";
        }
        return "redirect:/logout";
    }
}
