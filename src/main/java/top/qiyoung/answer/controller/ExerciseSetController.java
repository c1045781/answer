package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.DTO.ExerciseEditDTO;
import top.qiyoung.answer.DTO.ExerciseSetAndExercisesDTO;
import top.qiyoung.answer.DTO.ExerciseSetDTO;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.*;
import top.qiyoung.answer.service.ExerciseSetService;
import top.qiyoung.answer.service.SubjectService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public String addOrUpdate(HttpServletRequest request, ExerciseSetDTO setVM, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute("user");
        if (setVM.getExerciseSetId() != null) {
            setService.update(setVM);
        } else {
            setService.insert(setVM, user);
        }

        if (user.getRole() != 0 && user.getRole() != 1){
            response.setContentType("text/html; charset=utf-8");
            try {
                if (setVM.getExerciseSetId() != null){
                    response.getWriter().println("<script language='javascript'>alert('更新成功!');</script>");
                }else {
                    response.getWriter().println("<script language='javascript'>alert('添加成功!');</script>");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "redirect:/user/personal";
        }
        return "redirect:/exerciseSet/check";
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

    // 删除习题集
    @RequestMapping("/deleteExerciseSetForUser")
    @ResponseBody
    public String deleteExerciseSetForUser(Integer exerciseSetId) {
        setService.delete(exerciseSetId);
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

    // 获取ExerciseSetDTO
    @RequestMapping("/toUpdateByUser")
    @ResponseBody
    public ExerciseSetDTO toUpdateByUser(Integer exerciseSetId) {
//        setService.getExerciseSet(exerciseSetId);
        ExerciseSetDTO exerciseSetDTO = setService.getExerciseSetVMById(exerciseSetId);
        return exerciseSetDTO;
    }

    // 获取用户的套题list
    @RequestMapping("/getExerciseSetByUserId")
    @ResponseBody
    public PaginationDTO<ExerciseSetDTO> getExerciseSetByUserId(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                                                @RequestParam(value = "size", defaultValue = "2") Integer size,
                                                                HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        PaginationDTO<ExerciseSetDTO> paginationDTO = setService.getExerciseListByUserId(currentPage, size, "create_time desc", user);
        return paginationDTO;
    }

    // 根据套题Id获取多个题目
    @RequestMapping("/checkOfExerciseSet")
    @ResponseBody
    public PaginationDTO<ExerciseSetAndExercisesDTO> checkOfExerciseSet(Integer exerciseSetId, Integer currentPage, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        PaginationDTO<ExerciseSetAndExercisesDTO> dto = setService.checkOfExerciseSet(exerciseSetId,currentPage,user);
        return dto;
    }
}
