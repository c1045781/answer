package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.DTO.ExerciseEditDTO;
import top.qiyoung.answer.DTO.ExerciseSetDTO;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.*;
import top.qiyoung.answer.service.ExerciseSetService;
import top.qiyoung.answer.service.SubjectService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/exerciseSet")
public class ExerciseSetController {
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
                                @RequestParam(value = "size", defaultValue = "2") Integer size,
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
        User user = (User) request.getSession().getAttribute("user");
        if (user != null && (user.getRole() == 1 || user.getRole() == 0)){
            return "manage/exercise-set/exercise-set";
        }
        return "user/exercise-set";
    }

    // 添加或更新习题集
    @RequestMapping("/addOrUpdate")
    public String addOrUpdate(HttpServletRequest request, ExerciseSetDTO setVM) {
        User user = (User) request.getSession().getAttribute("user");
        if (setVM.getExerciseSetId() != null) {
            setService.update(setVM);
        } else {
            setService.insert(setVM, user);
        }

        return "redirect:/manage/exerciseSet/check";
    }

    // 删除习题集
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(Integer exerciseSetId) {
        int result = setService.delete(exerciseSetId);
        if (result <= 0) {
            return "failure";
        }
        return "success";
    }

    // 跳转更新页面
    @RequestMapping("/toUpdate")
    public String toUpdate(Integer exerciseSetId, Model model) {
//        setService.getExerciseSet(exerciseSetId);
        ExerciseSetDTO exerciseSetDTO = setService.getExerciseSetVMById(exerciseSetId);
        model.addAttribute("exerciseSetDTO", exerciseSetDTO);
        return "manage/exercise-set/add-exercise-set";
    }

}
