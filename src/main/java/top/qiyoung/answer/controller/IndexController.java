package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Resource
    private UserService userService;
    @Resource
    private ExerciseService exerciseService;
    @Resource
    private ExerciseSetService exerciseSetService;
    @Resource
    private CommentService commentService;
    @Resource
    private PermissionService permissionService;

    @RequestMapping("/answer.html")
    public String exercise(){
        return "user/answer";
    }

    @RequestMapping("/manage")
    public String manage(){
        return "manage/main-page";
    }

    // 跳转登陆页面
    @RequestMapping("/toLogin")
    public String signin(){
        return "login";
    }

    // 跳转注册页面
    @RequestMapping("/toRegister")
    public String signup(){
        return "register";
    }

    // 首页根据用户跳转页面
    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user != null){
            if(user.getRole() == 1 || user.getRole() == 0){
                int userCount = userService.userCount();
                int exerciseCount = exerciseService.countExercise();
                int exerciseSetCount = exerciseSetService.countExerciseSet();
                int commentCount = commentService.countComment();
                int permissionCount = permissionService.countPermission();
                model.addAttribute("userCount",userCount);
                model.addAttribute("exerciseCount",exerciseCount);
                model.addAttribute("exerciseSetCount",exerciseSetCount);
                model.addAttribute("commentCount",commentCount);
                model.addAttribute("permissionCount",permissionCount);
                return "manage/main-page";
            }
        }
        return "user/index";
    }
}
