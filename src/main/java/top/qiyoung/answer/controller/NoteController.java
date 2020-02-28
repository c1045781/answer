package top.qiyoung.answer.controller;

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
@RequestMapping("/note")
public class NoteController {
    @Resource
    private NoteService noteService;

    @RequestMapping("/getNote")
    @ResponseBody
    public Note getNote(Integer exerciseId, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        Note note = noteService.getNote(exerciseId, user.getUserId());
        return note;
    }

    @RequestMapping("/addOrUpdateNote")
    @ResponseBody
    public String addNote(HttpServletRequest request,@RequestBody Note note){
        User user = (User) request.getSession().getAttribute("user");
        note.setUserId(user.getUserId());
        note.setModifyTime(new Date());
        if (note.getNoteId() == null){
            note.setCreateTime(new Date());
            noteService.addNote(note);
        }else{
           noteService.updateNote(note);
        }
        return "success";
    }

    // 获取笔记列表
    @RequestMapping("/findNoteList")
    @ResponseBody
    public PaginationDTO<Note> findNoteList(HttpServletRequest request,
                                           Integer currentPage,
                                           @RequestParam(defaultValue = "3") Integer pageSize){
        User user = (User) request.getSession().getAttribute("user");
        PaginationDTO<Note> paginationDTO = noteService.findNoteList(user.getUserId(),currentPage,pageSize);
        return paginationDTO;
    }
}
