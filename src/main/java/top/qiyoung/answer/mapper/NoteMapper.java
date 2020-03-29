package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.qiyoung.answer.model.Note;

import java.util.List;

@Component
public interface NoteMapper {
    Note getNote(@Param("exerciseId") Integer exerciseId,@Param("userId") Integer userId);

    void addNote(Note note);

    void updateNote(Note note);

    Note findNoteByNoteId(Integer noteId);

    List<Note> findNoteList(@Param("userId") Integer userId, @Param("index") Integer index,@Param("pageSize") Integer pageSize);

    int countNoteList(Integer userId);

    void deleteByExerciseId(Integer exerciseId);

    void deleteByUserId(Integer userId);
}
