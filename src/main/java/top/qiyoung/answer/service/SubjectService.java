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
        return subjectMapper.getSubjectByBase(baseSubject);
    }

    public List<String> getBase() {
        return subjectMapper.getBase();
    }

    public Subject verification(String baseSubject, String subjectName) {
        return subjectMapper.verification(baseSubject, subjectName);
    }
}
