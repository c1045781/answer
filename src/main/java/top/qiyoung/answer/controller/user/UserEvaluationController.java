package top.qiyoung.answer.controller.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.dto.ResultDTO;
import top.qiyoung.answer.exception.CustomizeErrorCode;
import top.qiyoung.answer.model.Evaluation;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.service.EvaluationService;
import top.qiyoung.answer.service.ExerciseService;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/user/evaluation")
@Controller
public class UserEvaluationController {

    @Resource
    private EvaluationService evaluationService;
    @Resource
    private ExerciseService exerciseService;

    @RequestMapping("/addOrUpdate")
    @ResponseBody
    public ResultDTO addOrUpdate(Integer score, Integer exerciseId) {
        if (exerciseId != null){
            Exercise exercise = exerciseService.getExerciseByExerciseId(exerciseId);
            if (exercise != null){
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                evaluationService.addOrUpdate(score,exercise.getExerciseId(), userDetails);
            }else {
                return ResultDTO.errorOf(CustomizeErrorCode.EXERCISE_NOT_FOUND);
            }
        }else {
            return ResultDTO.errorOf(CustomizeErrorCode.EXERCISE_NOT_FOUND);
        }

        return ResultDTO.okOf();
    }

    // 查找用户收藏
    @RequestMapping("/scoreByExerciseIdList")
    @ResponseBody
    public List<Evaluation> scoreByExerciseIdList(@RequestBody List<String> exerciseId){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Evaluation> evaluations = evaluationService.scoreByUserIdAndExerciseId(exerciseId, userDetails);
        return evaluations;
    }
}
