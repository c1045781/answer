package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AccountController {

    @Resource
    private UserService userService;
    @PostMapping("/login")
    public String login(User user, RedirectAttributesModelMap model, HttpServletResponse response){
        User dbuser = userService.login(user);
        if(dbuser != null){
            response.addCookie(new Cookie("account",user.getAccount()));
            return "main-page";
        }
        model.addFlashAttribute("error","账号或密码错误");
        return "redirect:/";
    }

    @RequestMapping("/signout")
    public String signout(){
        return "signin";
    }
}
