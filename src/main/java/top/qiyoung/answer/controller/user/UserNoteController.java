package top.qiyoung.answer.controller.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.dto.PaginationDTO;
import top.qiyoung.answer.dto.ResultDTO;
import top.qiyoung.answer.exception.CustomizeErrorCode;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.Note;
import top.qiyoung.answer.service.ExerciseService;
import top.qiyoung.answer.service.NoteService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("/user/note")
public class UserNoteController {
    @Resource
    private NoteService noteService;
    @Resource
    private ExerciseService exerciseService;

    @RequestMapping("/getNote")
    @ResponseBody
    public Note getNote(Integer exerciseId, HttpServletRequest request){
//        MyUser myUser = (MyUser) request.getSession().getAttribute("myUser");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Note note = noteService.getNote(exerciseId, userDetails);
        return note;
    }

    @RequestMapping("/addOrUpdate")
    @ResponseBody
    public ResultDTO addNote(@RequestBody Note note){
        if (note.getExerciseId() != null) {
            Exercise exercise = exerciseService.getExerciseByExerciseId(note.getExerciseId());
            if (exercise != null) {
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                note.setExerciseId(exercise.getExerciseId());
                note.setModifyTime(new Date());
                if (note.getNoteId() == null) {
                    note.setCreateTime(new Date());
                    noteService.addNote(note, userDetails);
                } else {
                    noteService.updateNote(note, userDetails);
                }
            }else {
                return ResultDTO.errorOf(CustomizeErrorCode.EXERCISE_NOT_FOUND);
            }
        }else {
            return ResultDTO.errorOf(CustomizeErrorCode.EXERCISE_NOT_FOUND);
        }
        return ResultDTO.okOf();
    }

    // 获取笔记列表
    @RequestMapping("/findNoteList")
    @ResponseBody
    public PaginationDTO<Note> findNoteList(Integer currentPage,
                                           @RequestParam(defaultValue = "10") Integer pageSize){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PaginationDTO<Note> paginationDTO = noteService.findNoteList(userDetails,currentPage,pageSize);
        return paginationDTO;
    }
}
