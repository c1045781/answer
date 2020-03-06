package top.qiyoung.answer.controller.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.DTO.ExerciseSetAndExercisesDTO;
import top.qiyoung.answer.DTO.ExerciseSetDTO;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.Subject;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.ExerciseSetService;
import top.qiyoung.answer.service.SubjectService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user/exerciseSet")
public class UserExerciseSetController {
    @Resource
    private ExerciseSetService setService;
    @Resource
    private SubjectService subjectService;

    // 查询习题集
    @RequestMapping("/check")
    public String checkexercise(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                @RequestParam(value = "type", defaultValue = "title") String type,
                                @RequestParam(value = "subjectId", required = false) Integer subjectId,
                                @RequestParam(value = "search", required = false) String search,
                                @RequestParam(value = "orderby", defaultValue = "exercise_set_id asc") String orderby,
                                HttpServletRequest request,
                                Model model) {
        PaginationDTO<ExerciseSetDTO> paginationDTO = setService.getExerciseList(currentPage, size, type, subjectId, orderby, search);
        if (subjectId != null) {
            Subject subject = subjectService.getSubjectById(subjectId);
            List<Subject> subjects = subjectService.getSubjectByBase(subject.getBaseSubject());
            List<String> baseList = subjectService.getBase();
            model.addAttribute("subjectList", subjects);
            model.addAttribute("baseList", baseList);
        }
        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("type", type);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("search", search);
        model.addAttribute("orderby", orderby);
        return "user/exercise-set";
    }

    // 添加或更新习题集
    @RequestMapping("/addOrUpdate")
    public String addOrUpdate(HttpServletRequest request, ExerciseSetDTO setVM, HttpServletResponse response) {
//        User user = (User) request.getSession().getAttribute("user");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (setVM.getExerciseSetId() != null) {
            setService.update(setVM);
        } else {
            setService.insert(setVM, userDetails);
        }

            return "redirect:/user/personal";
    }

    // 删除习题集
    @RequestMapping("/deleteExerciseSetForUser")
    @ResponseBody
    public String deleteExerciseSetForUser(Integer exerciseSetId) {
        setService.delete(exerciseSetId);
        return "success";
    }

    // 获取ExerciseSetDTO
    @RequestMapping("/toUpdate")
    @ResponseBody
    public ExerciseSetDTO toUpdate(Integer exerciseSetId) {
//        setService.getExerciseSet(exerciseSetId);
        ExerciseSetDTO exerciseSetDTO = setService.getExerciseSetVMById(exerciseSetId);
        return exerciseSetDTO;
    }

    // 获取用户的套题list
    @RequestMapping("/getExerciseSetByUserId")
    @ResponseBody
    public PaginationDTO<ExerciseSetDTO> getExerciseSetByUserId(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                HttpServletRequest request){
//        User user = (User) request.getSession().getAttribute("user");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PaginationDTO<ExerciseSetDTO> paginationDTO = setService.getExerciseListByUserId(currentPage, size, "create_time desc", userDetails);
        return paginationDTO;
    }

    // 根据套题Id获取多个题目
    @RequestMapping("/checkOfExerciseSet")
    @ResponseBody
    public PaginationDTO<ExerciseSetAndExercisesDTO> checkOfExerciseSet(Integer exerciseSetId, Integer currentPage, HttpServletRequest request){
//        User user = (User) request.getSession().getAttribute("user");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PaginationDTO<ExerciseSetAndExercisesDTO> dto = setService.checkOfExerciseSet(exerciseSetId,currentPage,userDetails);
        return dto;
    }
}
