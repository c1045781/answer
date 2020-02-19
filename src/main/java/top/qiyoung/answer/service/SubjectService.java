package top.qiyoung.answer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.mapper.SubjectMapper;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.Query;
import top.qiyoung.answer.model.Subject;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SubjectService {

    @Resource
    private SubjectMapper subjectMapper;
    @Resource
    private ExerciseService exerciseService;


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

    @Transactional
    public int delete(Integer subjectId) {

        int result = subjectMapper.delete(subjectId);
        List<Exercise> exerciseList = exerciseService.getExerciseListBySubjectId(subjectId);
        for (Exercise exercise : exerciseList) {
         result = exerciseService.deleteById(exercise.getExerciseId());
        }
        return result;
    }

    public Integer update(Subject subject) {
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
