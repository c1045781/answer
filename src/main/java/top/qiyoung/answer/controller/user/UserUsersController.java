package top.qiyoung.answer.controller.user;

import org.apache.commons.lang3.StringUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/user/users")
public class UserUsersController {

    @Resource
    private UserService userService;

    @RequestMapping("/information")
    @ResponseBody
    public User information(HttpServletRequest request) {
//        User user = (User) request.getSession().getAttribute("user");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User dbUser = userService.getUserById(userDetails);
        return dbUser;
    }

    @RequestMapping("/personal/update")
    public String personalUpdate(User user, Model model,
                                 @RequestParam(value = "avatarImg", required = false) MultipartFile avatarImg,
                                 HttpServletResponse response) {
        if (avatarImg != null && StringUtils.isNotBlank(avatarImg.getOriginalFilename())) {
            String filename = avatarImg.getOriginalFilename();
            String[] split = filename.split("\\.");
            String suffix = split[split.length - 1];
            if (!avatarImg.isEmpty() && !(suffix.equals("jpg") || suffix.equals("png") || suffix.equals("jpeg"))) {
                response.setContentType("text/html; charset=utf-8");
                try {
                    response.getWriter().println("<script language='javascript'>alert('图片格式错误!');</script>");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "redirect:/user/personal";
            }
        }
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(user.getPhone());
        if (!m.matches()) {
            response.setContentType("text/html; charset=utf-8");
            try {
                response.getWriter().println("<script language='javascript'>alert('手机号格式错误!');</script>");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "redirect:/user/personal";
        }

        userService.update(user, avatarImg);
        return "redirect:/user/personal";
    }
}