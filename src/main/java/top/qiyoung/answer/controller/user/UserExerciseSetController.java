package top.qiyoung.answer.controller.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.qiyoung.answer.dto.ExerciseSetAndExercisesDTO;
import top.qiyoung.answer.dto.ExerciseSetDTO;
import top.qiyoung.answer.dto.PaginationDTO;
import top.qiyoung.answer.dto.ResultDTO;
import top.qiyoung.answer.exception.CustomizeErrorCode;
import top.qiyoung.answer.model.ExerciseSet;
import top.qiyoung.answer.model.Subject;
import top.qiyoung.answer.service.ExerciseSetService;
import top.qiyoung.answer.service.SubjectService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
                                Model model) {
        PaginationDTO<ExerciseSetDTO> paginationDTO = setService.getExerciseList(currentPage, size, type, subjectId, orderby, search);
        if (subjectId != null) {
            Subject subject = subjectService.getSubjectById(subjectId);
            List<Subject> subjects = subjectService.getSubjectByBase(subject.getBaseSubject());
            List<String> baseList = subjectService.getBase();
            model.addAttribute("subjectList", subjects);
            model.addAttribute("baseList", baseList);
        }
        List<ExerciseSetDTO> highLikeExerciseSet = setService.getHighLikeExerciseSet(subjectId);
        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("highLikeExerciseSet", highLikeExerciseSet);
        model.addAttribute("type", type);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("search", search);
        model.addAttribute("orderby", orderby);
        return "user/exercise-set";
    }

    // 添加或更新习题集
    @RequestMapping("/addOrUpdate")
    @ResponseBody
    public ResultDTO addOrUpdate(ExerciseSetDTO setVM) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (setVM.getExerciseSetId() != null) {
            setService.update(setVM);
        } else {
            setService.insert(setVM, userDetails);
        }
        return ResultDTO.okOf();
    }

    // 删除习题集
    @RequestMapping("/deleteExerciseSetForUser")
    @ResponseBody
    public ResultDTO deleteExerciseSetForUser(Integer exerciseSetId) {
        if (exerciseSetId != null) {
            ExerciseSet exerciseSet = setService.getExerciseSetByExerciseSetId(exerciseSetId);
            if (exerciseSet != null) {
                setService.delete(exerciseSet.getExerciseSetId());
            } else {
                return ResultDTO.errorOf(CustomizeErrorCode.EXERCISE_SET_NOT_FOUND);
            }
        }else {
            return ResultDTO.errorOf(CustomizeErrorCode.EXERCISE_SET_NOT_FOUND);
        }
        return ResultDTO.okOf();
    }

    // 获取ExerciseSetDTO
    @RequestMapping("/toUpdate")
    @ResponseBody
    public ExerciseSetDTO toUpdate(Integer exerciseSetId) {
        ExerciseSetDTO exerciseSetDTO = setService.getExerciseSetVMById(exerciseSetId);
        return exerciseSetDTO;
    }

    // 获取用户的套题list
    @RequestMapping("/getExerciseSetByUserId")
    @ResponseBody
    public PaginationDTO<ExerciseSetDTO> getExerciseSetByUserId(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                                                @RequestParam(value = "size", defaultValue = "10") Integer size){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PaginationDTO<ExerciseSetDTO> paginationDTO = setService.getExerciseListByUserId(currentPage, size, "create_time desc", userDetails);
        return paginationDTO;
    }

    // 根据套题Id获取多个题目
    @RequestMapping("/checkOfExerciseSet")
    @ResponseBody
    public PaginationDTO<ExerciseSetAndExercisesDTO> checkOfExerciseSet(Integer exerciseSetId, Integer currentPage){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PaginationDTO<ExerciseSetAndExercisesDTO> dto = setService.checkOfExerciseSet(exerciseSetId,currentPage, userDetails);
        return dto;
    }

    @RequestMapping("/addLike")
    @ResponseBody
    public ExerciseSetDTO addLike(Integer exerciseSetId){
        setService.addLike(exerciseSetId);
        ExerciseSetDTO exerciseSetDTO = setService.getExerciseSetVMById(exerciseSetId);
        return exerciseSetDTO;
    }

    @RequestMapping("/delLike")
    @ResponseBody
    public ExerciseSetDTO delLike(Integer exerciseSetId){
        setService.delLike(exerciseSetId);
        ExerciseSetDTO exerciseSetDTO = setService.getExerciseSetVMById(exerciseSetId);
        return exerciseSetDTO;
    }
}
