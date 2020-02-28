package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import top.qiyoung.answer.model.Note;

import java.util.List;

public interface NoteMapper {
    Note getNote(@Param("exerciseId") Integer exerciseId,@Param("userId") Integer userId);

    void addNote(Note note);

    void updateNote(Note note);

    Note findNoteByNoteId(Integer noteId);

    List<Note> findNoteList(@Param("userId") Integer userId, @Param("index") Integer index,@Param("pageSize") Integer pageSize);

    int countNoteList(Integer userId);
}
