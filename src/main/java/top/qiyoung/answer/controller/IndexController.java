package top.qiyoung.answer.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.service.*;

import javax.annotation.Resource;

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
    @RequestMapping("/index")
    public String index(Model model,@ModelAttribute("msg") String msg){
        if (StringUtils.isNotBlank(msg)){
            model.addAttribute("msg",msg);
        }
//        MyUser myUser = (MyUser) request.getSession().getAttribute("myUser");
        UserDetails userDetails;
        try {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return "user/index";
        }
        if (userDetails !=null) {
            MyUser myUser = userService.getUserByUserDetails(userDetails);
            if(myUser.getRole() == 0){
                int userCount = userService.userCount0();
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
            }else if (myUser.getRole() == 1){
                int userCount = userService.userCount1();
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
            else {
                model.addAttribute("myUser",myUser);
            }
        }
        return "user/index";
    }

    @RequestMapping("/user/personal")
    public String personal(@ModelAttribute("msg") String msg,Model model){
        if (StringUtils.isNotBlank(msg)){
            model.addAttribute("msg",msg);
        }
        return "user/personal";
    }

    @RequestMapping("/test")
    public String test(){
        return "/user/asd";
    }

    @RequestMapping("/user/modifyPassword")
    public String modifyPassword(){
        return "/user/modify-password";
    }

    @RequestMapping("/")
    public String index(){
        return "redirect:/index";
    }

}
