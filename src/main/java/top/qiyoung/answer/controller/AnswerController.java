package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import top.qiyoung.answer.model.Answer;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.AnswerService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("/answer")
public class AnswerController {

    @Resource
    private AnswerService answerService;

    @RequestMapping("/add")
    public void add(@RequestBody Answer answer, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        answer.setUserId(user.getUserId());
        answer.setCreateTime(new Date());
        answerService.add(answer);
    }
}
