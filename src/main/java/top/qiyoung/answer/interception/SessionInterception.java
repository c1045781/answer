package top.qiyoung.answer.interception;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.User;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Service
public class SessionInterception implements HandlerInterceptor {
    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        Cookie[] cookies = request.getCookies();
        User user = new User();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("account")) {
                    String account = cookie.getValue();
                    user.setAccount(account);
                    User dbuser = userMapper.findUserByAccount(account);

                    if (dbuser != null) {
                        HttpSession session = request.getSession();
                        session.setMaxInactiveInterval(60 * 60 * 24 * 30);
                        session.setAttribute("user", dbuser);
                    }
                    break;
                }
            }
        }
        /*if (user == null){
            response.sendRedirect("/toSignin");
            return false;
        }*/

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
