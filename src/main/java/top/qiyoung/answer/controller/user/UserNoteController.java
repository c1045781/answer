package top.qiyoung.answer.controller.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.Note;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.NoteService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("/user/note")
public class UserNoteController {
    @Resource
    private NoteService noteService;

    @RequestMapping("/getNote")
    @ResponseBody
    public Note getNote(Integer exerciseId, HttpServletRequest request){
//        User user = (User) request.getSession().getAttribute("user");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Note note = noteService.getNote(exerciseId, userDetails);
        return note;
    }

    @RequestMapping("/addOrUpdate")
    @ResponseBody
    public String addNote(HttpServletRequest request,@RequestBody Note note){
//        User user = (User) request.getSession().getAttribute("user");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        note.setUserId(user.getUserId());
        note.setModifyTime(new Date());
        if (note.getNoteId() == null){
            note.setCreateTime(new Date());
            noteService.addNote(note,userDetails);
        }else{
           noteService.updateNote(note,userDetails);
        }
        return "success";
    }

    // 获取笔记列表
    @RequestMapping("/findNoteList")
    @ResponseBody
    public PaginationDTO<Note> findNoteList(HttpServletRequest request,
                                           Integer currentPage,
                                           @RequestParam(defaultValue = "10") Integer pageSize){
//        User user = (User) request.getSession().getAttribute("user");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PaginationDTO<Note> paginationDTO = noteService.findNoteList(userDetails,currentPage,pageSize);
        return paginationDTO;
    }
}
