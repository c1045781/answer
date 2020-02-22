package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.DTO.ExerciseEditDTO;
import top.qiyoung.answer.DTO.HistoryAnswerDTO;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.Answer;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.AnswerService;
import top.qiyoung.answer.service.ExerciseService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/answer")
public class AnswerController {

    @Resource
    private AnswerService answerService;
    @Resource
    private ExerciseService exerciseService;

    // 添加用户答题记录
    @RequestMapping("/addOrUpdate")
    @ResponseBody
    public void addOrUpdate(@RequestBody Answer answer, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        answer.setUserId(user.getUserId());
        answer.setCreateTime(new Date());
        answerService.addOrUpdate(answer);
    }

    // 查找用户答题记录
    @RequestMapping("/findAnswer")
    @ResponseBody
    public PaginationDTO<HistoryAnswerDTO> findHistoryAnswer(HttpServletRequest request,
                                             Integer currentPage,
                                             @RequestParam(defaultValue = "3") Integer pageSize){
        User user = (User) request.getSession().getAttribute("user");
        PaginationDTO<HistoryAnswerDTO> paginationDTO = answerService.findHistoryAnswer(user.getUserId(),currentPage,pageSize);
        return paginationDTO;
    }

    // 查找用户答题记录
    @RequestMapping("/viewHistoryExercise")
    public String viewHistoryExercise(HttpServletRequest request, Integer exerciseId, Model model){
        User user = (User) request.getSession().getAttribute("user");

        Answer answer = answerService.findAnswerByExerciseIdAndUserId(exerciseId,user.getUserId());
        ExerciseEditDTO exerciseEditDTO = exerciseService.getExerciseEdit(exerciseId);
        List<ExerciseEditDTO> exerciseEditDTOList = new ArrayList();
        exerciseEditDTOList.add(exerciseEditDTO);
        model.addAttribute("answer",answer);
        model.addAttribute("exerciseEditDTOList", exerciseEditDTOList);
        return "user/answer";
    }
}
