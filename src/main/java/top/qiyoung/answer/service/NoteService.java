package top.qiyoung.answer.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.mapper.NoteMapper;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.Note;
import top.qiyoung.answer.model.User;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NoteService {

    @Resource
    private NoteMapper noteMapper;
    @Resource
    private UserMapper userMapper;

    public Note getNote(Integer exerciseId, UserDetails userDetails) {
        User user = userMapper.findUserByAccount(userDetails.getUsername());
        return noteMapper.getNote(exerciseId,user.getUserId());
    }

    public void addNote(Note note,UserDetails userDetails) {
        User user = userMapper.findUserByAccount(userDetails.getUsername());
        note.setUserId(user.getUserId());
        noteMapper.addNote(note);
    }

    public void updateNote(Note note,UserDetails userDetails) {
        User user = userMapper.findUserByAccount(userDetails.getUsername());
        note.setUserId(user.getUserId());
        noteMapper.updateNote(note);
    }

    public Note findNoteByNoteId(Integer noteId) {
        return noteMapper.findNoteByNoteId(noteId);
    }

    public PaginationDTO<Note> findNoteList(UserDetails userDetails, Integer currentPage, Integer pageSize) {
        User user = userMapper.findUserByAccount(userDetails.getUsername());
        List<Note> list = noteMapper.findNoteList(user.getUserId(),(currentPage-1)*pageSize,pageSize);
        int count = noteMapper.countNoteList(user.getUserId());
        PaginationDTO<Note> paginationDTO = new PaginationDTO<>(currentPage,pageSize,(int)Math.ceil((double) count /(double) pageSize),count,null,null,list);
        return paginationDTO;
    }
}
