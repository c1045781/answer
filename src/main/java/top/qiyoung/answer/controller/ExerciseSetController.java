package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.model.ExerciseSet;
import top.qiyoung.answer.model.ExerciseSetVM;
import top.qiyoung.answer.service.ExerciseSetService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/exerciseSet")
public class ExerciseSetController {
    @Resource
    private ExerciseSetService setService;
    @RequestMapping("/add")
    public String addexercise() {
        return "exercise-set/add-exercise-set";
    }

    @RequestMapping("/check")
    public String checkexercise() {
        List<ExerciseSet> exerciseSetList = setService.getAll();
        return "exercise-set/exercise-set";
    }

    @RequestMapping("/addOrUpdate")
    public String addOrUpdate(HttpServletRequest request, ExerciseSetVM setVM) {
        ExerciseSet set = new ExerciseSet();
        User user = (User) request.getSession().getAttribute("user");
        setService.addOrUpdate(setVM,user);
        return "redirect:/exerciseSet/check";
    }
}
