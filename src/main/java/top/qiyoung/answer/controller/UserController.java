package top.qiyoung.answer.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.ExerciseService;
import top.qiyoung.answer.service.ExerciseSetService;
import top.qiyoung.answer.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    // 查询用户
    @RequestMapping("/check")
    public String checkUser(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                            @RequestParam(value = "size", defaultValue = "2") Integer size,
                            @RequestParam(value = "search", required = false) String search,
                            @RequestParam(value = "type", required = false) String type,
                            @RequestParam(value = "role", required = false) Integer role,
                            @RequestParam(value = "order", defaultValue = "user_id asc") String order,
                            Model model) {
        PaginationDTO paginationDTO = userService.getUserList(currentPage, size, search, type, order,role);
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
    public String addOrUpdate(User user, Model model,
                              @RequestParam(value = "avatarImg", required = false) MultipartFile avatarImg){

        String filename = avatarImg.getOriginalFilename();
        String[] split = filename.split("\\.");
        String suffix = split[split.length - 1];
        if (!avatarImg.isEmpty() && !(suffix.equals("jpg") || suffix.equals("png") || suffix.equals("jpeg"))) {
            model.addAttribute("message", "图片格式错误");
            model.addAttribute("user", user);
            return "manage/user/add-user";
        }
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (user != null){
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(user.getPhone());
            if (!m.matches()){
                model.addAttribute("message", "手机号格式错误");
                model.addAttribute("user", user);
                return "manage/user/add-user";
            }
        }

        if (user.getUserId() == null) {
            int insert = userService.insert(user, avatarImg);
            if (insert == 0) {
                model.addAttribute("message", "添加失败,账号已存在");
                return "manage/user/add-user";
            } else if (insert == 2) {
                model.addAttribute("message", "图片上传失败请重新上传");
                return "manage/user/add-user";
            } else {
                return "redirect:/user/check";
            }
        } else {
            int result = userService.update(user, avatarImg);
            if (result <= 0){
                User dbuser = userService.getUserById(user.getUserId());
                model.addAttribute("user", dbuser);
                return "manage/user/add-user";
            }
            return "redirect:/user/check";
        }
    }

    // 跳转更新页面
    @RequestMapping("/toUpdate")
    public String toUpdate(Integer userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "manage/user/add-user";
    }

    // 删除用户
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(Integer userId) {
        int result = userService.deleteById(userId);
        if(result<=0){
            return "failure";
        }
        return "success";
    }

    @RequestMapping("/information")
    @ResponseBody
    public User information(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        User dbUser = userService.getUserById(user.getUserId());
        return dbUser;
    }

    @RequestMapping("/personal")
    public String personal(){
        return "user/personal";
    }

    @RequestMapping("/personal/update")
    public String personalUpdate(User user, Model model,
                                 @RequestParam(value = "avatarImg", required = false) MultipartFile avatarImg){
        if (avatarImg != null && StringUtils.isNotBlank(avatarImg.getOriginalFilename())) {
            String filename = avatarImg.getOriginalFilename();
            String[] split = filename.split("\\.");
            String suffix = split[split.length - 1];
            if (!avatarImg.isEmpty() && !(suffix.equals("jpg") || suffix.equals("png") || suffix.equals("jpeg"))) {
                model.addAttribute("message", "图片格式错误");
                model.addAttribute("user", user);
                return "user/personal";
            }
        }
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(user.getPhone());
        if (!m.matches()){
            model.addAttribute("message", "手机号格式错误");
            model.addAttribute("user", user);
            return "user/personal";
        }

        userService.update(user, avatarImg);
        return "redirect:/user/personal";
    }
}