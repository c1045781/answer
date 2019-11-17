package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.ExerciseSetVM;
import top.qiyoung.answer.model.Pagination;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.ExerciseSetService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
    public String checkexercise(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                @RequestParam(value = "title", required = false) String title,
                                @RequestParam(value = "subjectId", required = false) Integer subjectId,
                                @RequestParam(value = "userId", required = false) Integer userId,
                                @RequestParam(value = "order", defaultValue = "exercise_set_id asc") String order,
                                Model model) {
        Pagination<ExerciseSetVM> pagination = setService.getAll(currentPage, size, title, subjectId, userId, order);

        model.addAttribute("pagination",pagination);
        model.addAttribute("title",title);
        model.addAttribute("subjectId",subjectId);
        model.addAttribute("userId",userId);
        return "exercise-set/exercise-set";
    }

    @RequestMapping("/addOrUpdate")
    public String addOrUpdate(HttpServletRequest request, ExerciseSetVM setVM) {
        User user = (User) request.getSession().getAttribute("user");
        if (setVM.getExerciseSetId() != null){
            setService.update(setVM);
        }else{
            setService.insert(setVM, user);
        }

        return "redirect:/exerciseSet/check";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(Integer exerciseSetId){
        setService.delete(exerciseSetId);
        return "success";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer exerciseSetId,Model model){
//        setService.getExerciseSet(exerciseSetId);
        ExerciseSetVM exerciseSetVM = setService.getExerciseSetVMById(exerciseSetId);
        model.addAttribute("exerciseSetVM",exerciseSetVM);
        return "exercise-set/add-exercise-set";
    }
}
