package top.qiyoung.answer.controller.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.model.Evaluation;
import top.qiyoung.answer.service.EvaluationService;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/user/evaluation")
@Controller
public class UserEvaluationController {

    @Resource
    private EvaluationService evaluationService;

    @RequestMapping("/addOrUpdate")
    @ResponseBody
    public String addOrUpdate(Integer score,Integer exerciseId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        evaluationService.addOrUpdate(score,exerciseId,userDetails);
        return "success";
    }

    // 查找用户收藏
    @RequestMapping("/scoreByExerciseIdList")
    @ResponseBody
    public List<Evaluation> scoreByExerciseIdList(@RequestBody List<String> exerciseId){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Evaluation> evaluations = evaluationService.scoreByUserIdAndExerciseId(exerciseId,userDetails);
        return evaluations;
    }
}
