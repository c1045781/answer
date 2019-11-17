package top.qiyoung.answer.service;

import org.springframework.stereotype.Service;
import top.qiyoung.answer.mapper.SubjectMapper;
import top.qiyoung.answer.model.Subject;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SubjectService {

    @Resource
    private SubjectMapper subjectMapper;


    public List<Subject> getSubjectByBase(String baseSubject) {
        if (baseSubject != null || !baseSubject.equals("")) {
            return subjectMapper.getSubjectByBase(baseSubject);
        }
        return null;
    }

    public List<String> getBase() {
        return subjectMapper.getBase();
    }

    public Subject verification(String baseSubject, String subjectName) {
        return subjectMapper.verification(baseSubject, subjectName);
    }

    public Subject getSubjectById(Integer subjectId) {
        Subject subject = subjectMapper.getSubjectById(subjectId);
        return subject;
    }
}
