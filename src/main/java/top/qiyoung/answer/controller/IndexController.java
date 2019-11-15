package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Resource
    private UserService userService;

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user != null){
            int userCount = userService.userCount();
            model.addAttribute("userCount",userCount);
            return "main-page";
        }
        return "signin";
    }
}
