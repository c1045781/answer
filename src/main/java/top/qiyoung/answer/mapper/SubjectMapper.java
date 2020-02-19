package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.Query;
import top.qiyoung.answer.model.Subject;

import java.util.List;

public interface SubjectMapper {
    List<Subject> getSubjectByBase(String baseSubject);
    List<String> getBase();

    Subject getSubjectById(Integer exerciseContentId);

    Subject verification(@Param("baseSubject") String baseSubject,@Param("subjectName") String subjectName);

    List<Subject> getSubjectList(Query query);
    Integer countSubjectList(Query query);

    Integer delete(Integer subjectId);

    Integer update(Subject subject);

    Integer insert(Subject subject);

    Subject getSubject(Subject subject);
}
