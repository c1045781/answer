package top.qiyoung.answer.controller.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import top.qiyoung.answer.DTO.ExerciseEditDTO;
import top.qiyoung.answer.DTO.HistoryAnswerDTO;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.Answer;
import top.qiyoung.answer.model.Note;
import top.qiyoung.answer.model.Subject;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.AnswerService;
import top.qiyoung.answer.service.ExerciseService;
import top.qiyoung.answer.service.NoteService;
import top.qiyoung.answer.service.SubjectService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user/answer")
public class UserAnswerController {

    @Resource
    private AnswerService answerService;
    @Resource
    private ExerciseService exerciseService;
    @Resource
    private SubjectService subjectService;
    @Resource
    private NoteService noteService;

    // 添加用户答题记录
    @RequestMapping("/addOrUpdate")
    @ResponseBody
    public void addOrUpdate(@RequestBody Answer answer, HttpServletRequest request){
//        User user = (User) request.getSession().getAttribute("user");
//        answer.setUserId(user.getUserId());
//        answer.setCreateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        answerService.addOrUpdate(answer,userDetails);
    }

    // 查找用户答题记录
    @RequestMapping("/findAnswer")
    @ResponseBody
    public PaginationDTO<HistoryAnswerDTO> findHistoryAnswer(HttpServletRequest request,
                                                             Integer currentPage,
                                                             @RequestParam(defaultValue = "10") Integer pageSize){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = (User) request.getSession().getAttribute("user");
        PaginationDTO<HistoryAnswerDTO> paginationDTO = answerService.findHistoryAnswer(userDetails,currentPage,pageSize);

        return paginationDTO;
    }

    // 查找用户答题记录
    @RequestMapping("/viewHistoryExercise")
    public String viewHistoryExercise(Integer answerId, Model model){

        Answer answer = answerService.findAnswerByAnswerId(answerId);
        ExerciseEditDTO exerciseEditDTO = exerciseService.getExerciseEdit(answer.getExerciseId());
        List<ExerciseEditDTO> exerciseEditDTOList = new ArrayList();
        exerciseEditDTOList.add(exerciseEditDTO);
        model.addAttribute("answer",answer);
        model.addAttribute("exerciseEditDTOList", exerciseEditDTOList);
        return "user/answer";
    }

    @RequestMapping("/wrongBook")
    public String error(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "subjectId", required = false) Integer subjectId,
                        HttpServletRequest request,
                        Model model,
                        RedirectAttributesModelMap redirectAttributesModelMap){
//        User user = (User) request.getSession().getAttribute("user");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       /* if (user == null){
            redirectAttributesModelMap.addFlashAttribute("error","请先登录账号");
            return "redirect:/toLogin";
        }*/
        PaginationDTO<HistoryAnswerDTO> paginationDTO = answerService.findWrongAnswer(userDetails,currentPage,pageSize,search,subjectId);

        if (subjectId != null) {
            Subject subject = subjectService.getSubjectById(subjectId);
            List<Subject> subjects = subjectService.getSubjectByBase(subject.getBaseSubject());
            List<String> baseList = subjectService.getBase();
            model.addAttribute("subjectList",subjects);
            model.addAttribute("baseList",baseList);
        }

        model.addAttribute("paginationDTO",paginationDTO);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("search", search);
        return "user/wrong-book";
    }

    // 查找用户答题记录
    @RequestMapping("/viewNoteExercise")
    public String viewNoteExercise(Integer noteId, Model model){
        Note note = noteService.findNoteByNoteId(noteId);
        Answer answer = answerService.findAnswerByExerciseIdAndUserId(note.getExerciseId(),note.getUserId());
        ExerciseEditDTO exerciseEditDTO = exerciseService.getExerciseEdit(answer.getExerciseId());
        List<ExerciseEditDTO> exerciseEditDTOList = new ArrayList();
        exerciseEditDTOList.add(exerciseEditDTO);
        model.addAttribute("answer",answer);
        model.addAttribute("exerciseEditDTOList", exerciseEditDTOList);
        return "user/answer";
    }
}
