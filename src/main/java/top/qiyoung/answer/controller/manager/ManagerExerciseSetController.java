package top.qiyoung.answer.controller.manager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.qiyoung.answer.dto.ExerciseSetAndExercisesDTO;
import top.qiyoung.answer.dto.ExerciseSetDTO;
import top.qiyoung.answer.dto.PaginationDTO;
import top.qiyoung.answer.dto.ResultDTO;
import top.qiyoung.answer.exception.CustomizeErrorCode;
import top.qiyoung.answer.exception.CustomizeException;
import top.qiyoung.answer.model.ExerciseSet;
import top.qiyoung.answer.model.Subject;
import top.qiyoung.answer.service.ExerciseSetService;
import top.qiyoung.answer.service.SubjectService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/manager/exerciseSet")
public class ManagerExerciseSetController {
    @Resource
    private ExerciseSetService setService;
    @Resource
    private SubjectService subjectService;

    // 跳转添加习题集页面
    @RequestMapping("/add")
    public String addexercise() {
        return "manage/exercise-set/add-exercise-set";
    }

    // 查询习题集
    @RequestMapping("/check")
    public String checkexercise(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                @RequestParam(value = "type", defaultValue = "title") String type,
                                @RequestParam(value = "subjectId", required = false) Integer subjectId,
                                @RequestParam(value = "search", required = false) String search,
                                @RequestParam(value = "orderby", defaultValue = "exercise_set_id asc") String orderby,
                                @ModelAttribute("msg") String msg,
                                Model model) {
        PaginationDTO<ExerciseSetDTO> paginationDTO = setService.getExerciseList(currentPage, size, type, subjectId, orderby, search);
        if (subjectId != null) {
            Subject subject = subjectService.getSubjectById(subjectId);
            if (subject == null){
                throw new CustomizeException(CustomizeErrorCode.SUBJECT_NOT_FOUND);
            }
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

        if (StringUtils.isNotBlank(msg)){
            model.addAttribute("msg",msg);
        }
        return "manage/exercise-set/exercise-set";
    }

    // 添加或更新习题集
    @RequestMapping("/addOrUpdate")
    public String addOrUpdate(ExerciseSetDTO setVM, RedirectAttributes redirectAttributes) {
//        MyUser myUser = (MyUser) request.getSession().getAttribute("myUser");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (setVM.getExerciseSetId() != null) {
            setService.update(setVM);
        } else {
            setService.insert(setVM, userDetails);
        }
        redirectAttributes.addFlashAttribute("msg","操作成功");
        return "redirect:/manager/exerciseSet/check";
    }

    // 删除习题集
    @RequestMapping("/delete")
    @ResponseBody
    public ResultDTO delete(Integer exerciseSetId) {
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

    // 跳转更新页面
    @RequestMapping("/toUpdate")
    public String toUpdate(Integer exerciseSetId, Model model) {
//        setService.getExerciseSet(exerciseSetId);
        ExerciseSetDTO exerciseSetDTO = setService.getExerciseSetVMById(exerciseSetId);
        model.addAttribute("exerciseSetDTO", exerciseSetDTO);
        return "manage/exercise-set/add-exercise-set";
    }

    // 根据套题Id获取多个题目
    @RequestMapping("/checkOfExerciseSet")
    @ResponseBody
    public PaginationDTO<ExerciseSetAndExercisesDTO> checkOfExerciseSet(Integer exerciseSetId, Integer currentPage){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PaginationDTO<ExerciseSetAndExercisesDTO> dto = setService.checkOfExerciseSet(exerciseSetId,currentPage, userDetails);
        return dto;
    }


}
