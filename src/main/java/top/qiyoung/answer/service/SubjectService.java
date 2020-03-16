package top.qiyoung.answer.service;

import org.springframework.stereotype.Service;
import top.qiyoung.answer.dto.PaginationDTO;
import top.qiyoung.answer.exception.CustomizeErrorCode;
import top.qiyoung.answer.exception.CustomizeException;
import top.qiyoung.answer.mapper.ExerciseMapper;
import top.qiyoung.answer.mapper.ExerciseSetMapper;
import top.qiyoung.answer.mapper.SubjectMapper;
import top.qiyoung.answer.model.Query;
import top.qiyoung.answer.model.Subject;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SubjectService {

    @Resource
    private SubjectMapper subjectMapper;
    @Resource
    private ExerciseMapper exerciseMapper;
    @Resource
    private ExerciseSetMapper exerciseSetMapper;


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
        if (subject == null){
            throw new CustomizeException(CustomizeErrorCode.SUBJECT_NOT_FOUND);
        }
        return subject;
    }

    public PaginationDTO<Subject> getSubjectList(Integer currentPage, Integer size, String type, String search, String order) {
        Query query = new Query();
        query.setIndex((currentPage - 1) * size);
        query.setSize(size);
        query.setSearch(search);
        query.setType(type);
        query.setOrder(order);
        List<Subject> subjectList = subjectMapper.getSubjectList(query);
        query.setIndex(null);
        query.setSize(null);
        Integer count = subjectMapper.countSubjectList(query);

        PaginationDTO<Subject> paginationDTO = new PaginationDTO<>(currentPage, size);
        paginationDTO.setDataList(subjectList);
        paginationDTO.setTotalSize(count);
        paginationDTO.setTotalPage((int) Math.ceil((double) paginationDTO.getTotalSize() / (double) size));
        return paginationDTO;
    }


    public void delete(Integer subjectId) {
        exerciseMapper.deleteBySubjectId(subjectId);
        exerciseSetMapper.deleteBySubjectId(subjectId);
        subjectMapper.delete(subjectId);
    }

    public Integer update(Subject subject) {
        Subject dbSubject = subjectMapper.getSubjectById(subject.getSubjectId());
        if (dbSubject == null){
            throw new CustomizeException(CustomizeErrorCode.SUBJECT_NOT_FOUND);
        }
        return subjectMapper.update(subject);
    }

    public Integer insert(Subject subject) {
        Subject dbSubject = subjectMapper.getSubject(subject);
        if (dbSubject == null) {
            return subjectMapper.insert(subject);
        } else {
            return -1;
        }
    }
}
