package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AccountController {

    @Resource
    private UserService userService;

    // 用户登录
   /* @PostMapping("/login")
    public String login(User user, RedirectAttributesModelMap model, HttpServletResponse response){
        User dbuser = userService.login(user);
        if(dbuser != null){
            response.addCookie(new Cookie("account",user.getAccount()));
            return "redirect:/";
        }
        model.addFlashAttribute("error","账号或密码错误");
        return "redirect:/toLogin";
    }*/

    // 用户注册
    @RequestMapping("/register")
    public String register(User user, RedirectAttributesModelMap model){
        if (user.getPassword().length() < 6){
            model.addFlashAttribute("error","密码长度最少6个字符");
            return "redirect:/toRegister";
        }
        user.setRole(2);
        user.setSex("男");
        int i = userService.insert(user, null);
        if (i == 0){
            model.addFlashAttribute("error","账号已存在");
            return "redirect:/toRegister";
        }
        return "redirect:/toLogin";
    }


    // 用户登出
    @RequestMapping("/logout")
    public String signout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");
        for (Cookie cookie : request.getCookies()) {
            if ("account".equals(cookie.getName())){
                cookie.setMaxAge(0);
                cookie.setPath(cookie.getPath());
                response.addCookie(cookie);
            }
        }
        return "user/index";
    }


}
