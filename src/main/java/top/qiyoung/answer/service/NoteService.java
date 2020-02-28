package top.qiyoung.answer.service;

import org.springframework.stereotype.Service;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.mapper.NoteMapper;
import top.qiyoung.answer.model.Note;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NoteService {

    @Resource
    private NoteMapper noteMapper;

    public Note getNote(Integer exerciseId, Integer userId) {
        return noteMapper.getNote(exerciseId,userId);
    }

    public void addNote(Note note) {
        noteMapper.addNote(note);
    }

    public void updateNote(Note note) {
        noteMapper.updateNote(note);
    }

    public Note findNoteByNoteId(Integer noteId) {
        return noteMapper.findNoteByNoteId(noteId);
    }

    public PaginationDTO<Note> findNoteList(Integer userId, Integer currentPage, Integer pageSize) {
        List<Note> list = noteMapper.findNoteList(userId,(currentPage-1)*pageSize,pageSize);
        int count = noteMapper.countNoteList(userId);
        PaginationDTO<Note> paginationDTO = new PaginationDTO<>(currentPage,pageSize,(int)Math.ceil((double) count /(double) pageSize),count,null,null,list);
        return paginationDTO;
    }
}
