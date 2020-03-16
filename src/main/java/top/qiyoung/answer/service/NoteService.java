package top.qiyoung.answer.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.dto.PaginationDTO;
import top.qiyoung.answer.exception.CustomizeErrorCode;
import top.qiyoung.answer.exception.CustomizeException;
import top.qiyoung.answer.mapper.NoteMapper;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.model.Note;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NoteService {

    @Resource
    private NoteMapper noteMapper;
    @Resource
    private UserMapper userMapper;

    public Note getNote(Integer exerciseId, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        return noteMapper.getNote(exerciseId, myUser.getUserId());
    }

    public void addNote(Note note,UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        note.setUserId(myUser.getUserId());
        noteMapper.addNote(note);
    }

    public void updateNote(Note note, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        note.setUserId(myUser.getUserId());
        noteMapper.updateNote(note);
    }

    public Note findNoteByNoteId(Integer noteId) {
        Note note = noteMapper.findNoteByNoteId(noteId);
        if (note == null){
            throw new CustomizeException(CustomizeErrorCode.NOTE_NOT_FOUND);
        }
        return note;
    }

    public PaginationDTO<Note> findNoteList(UserDetails userDetails, Integer currentPage, Integer pageSize) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        List<Note> list = noteMapper.findNoteList(myUser.getUserId(),(currentPage-1)*pageSize,pageSize);
        int count = noteMapper.countNoteList(myUser.getUserId());
        PaginationDTO<Note> paginationDTO = new PaginationDTO<>(currentPage,pageSize,(int)Math.ceil((double) count /(double) pageSize),count,null,null,list);
        return paginationDTO;
    }
}
