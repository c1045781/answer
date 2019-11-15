package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/exercises-set")
public class ExercisesSetController {
    @RequestMapping("/add")
    public String addExercises(){
        return "exercises-set/add-exercises-set";
    }
    @RequestMapping("/check")
    public String checkExercises(){
        return "exercises-set/exercises-set";
    }
}
