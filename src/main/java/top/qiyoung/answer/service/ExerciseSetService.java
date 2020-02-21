package top.qiyoung.answer.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.DTO.ExerciseSetDTO;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.mapper.*;
import top.qiyoung.answer.model.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExerciseSetService {
    @Resource
    private ExerciseSetMapper setMapper;
    @Resource
    private ExerciseMapper exerciseMapper;
    @Resource
    private MidMapper midMapper;
    @Resource
    private SubjectMapper subjectMapper;
    @Resource
    private UserMapper userMapper;

    public int insert(ExerciseSetDTO setVM, User user) {
        int result;
        ExerciseSet exerciseSet = new ExerciseSet();

        exerciseSet.setCreateUserId(user.getUserId());
        exerciseSet.setCreateTime(new Date());
        exerciseSet.setModifyTime(new Date());
        List<Integer> exerciseIds = new ArrayList<>();

        exerciseSet.setExerciseCount(exerciseIds.size());
        exerciseSet.setSubjectId(setVM.getSubject().getSubjectId());
        exerciseSet.setTitle(setVM.getTitle());
        int exerciseSetId = 1;
        Integer lastId = setMapper.getLastId();
        if (lastId != null) {
            exerciseSetId = lastId + 1;
        }
        exerciseSet.setExerciseSetId(exerciseSetId);
        result = setMapper.insert(exerciseSet);
        if (setVM.getExerciseList() != null) {
            for (Exercise exercise : setVM.getExerciseList()) {
                exerciseIds.add(exercise.getExerciseId());
            }
            for (Integer exerciseId : exerciseIds) {
                result = midMapper.insert(exerciseSetId, exerciseId);
            }
        }
        return result;
    }

    public int update(ExerciseSetDTO setVM) {
        int result;

        midMapper.deleteByExerciseSetId(setVM.getExerciseSetId());

        ExerciseSet exerciseSet = new ExerciseSet();
        List<Integer> exerciseIds = new ArrayList<>();
        if (setVM.getExerciseList() != null){
            for (Exercise exercise : setVM.getExerciseList()) {
                exerciseIds.add(exercise.getExerciseId());
            }
        }
        exerciseSet.setExerciseSetId(setVM.getExerciseSetId());
        exerciseSet.setTitle(setVM.getTitle());
        exerciseSet.setSubjectId(setVM.getSubject().getSubjectId());
        exerciseSet.setExerciseCount(exerciseIds.size());
        exerciseSet.setModifyTime(new Date());

        result = setMapper.update(exerciseSet);
        for (Integer exerciseId : exerciseIds) {
            result = midMapper.insert(setVM.getExerciseSetId(), exerciseId);
        }
        return result;
    }

    public PaginationDTO<ExerciseSetDTO> getExerciseList(Integer currentPage, Integer size, String type, Integer subjectId, String order, String search) {
        PaginationDTO<ExerciseSetDTO> paginationDTO = new PaginationDTO<>(currentPage, size);
        Query query = new Query();
        query.setIndex((currentPage - 1) * size);
        query.setSize(size);
        query.setOrder(order);
        query.setId(subjectId);
        query.setType(type);
        List<ExerciseSet> exerciseSets = new ArrayList<>();
        int count = 0;
        if (type != null && type.equals("createUser")) {
            List<User> users = userMapper.getUserByUsername(search);
            List<ExerciseSet> exerciseList = new ArrayList<>();
            for (User dbuser : users) {
                query.setIndex(null);
                query.setSize(null);
                query.setSearch(dbuser.getUserId() + "");
                List<ExerciseSet> temp = setMapper.getExerciseSetList(query);
                for (ExerciseSet set : temp) {
                    exerciseList.add(set);
                }
                count += setMapper.countExerciseSetList(query);
            }
            int index = (currentPage - 1) * size;
            int length;
            if (exerciseList.size() < (index + size)) {
                length = exerciseList.size();
            } else {
                length = index + size;
            }
            for (int i = index; i < length; i++) {
                exerciseSets.add(exerciseList.get(i));
            }
        } else {
            query.setSearch(search);
            exerciseSets = setMapper.getExerciseSetList(query);
            query.setIndex(null);
            query.setSize(null);
            count = setMapper.countExerciseSetList(query);
        }
        List<ExerciseSetDTO> vms = new ArrayList<>();

        for (ExerciseSet exerciseSet : exerciseSets) {
            ExerciseSetDTO exerciseSetDTO = new ExerciseSetDTO();
            Subject subject = subjectMapper.getSubjectById(exerciseSet.getSubjectId());
            User user = userMapper.getUserById(exerciseSet.getCreateUserId());
            BeanUtils.copyProperties(exerciseSet, exerciseSetDTO);
            exerciseSetDTO.setUser(user);
            exerciseSetDTO.setSubject(subject);
            vms.add(exerciseSetDTO);
        }
        paginationDTO.setDataList(vms);
        paginationDTO.setTotalSize(count);
        paginationDTO.setTotalPage((int) Math.ceil((double) paginationDTO.getTotalSize() / (double) size));
        return paginationDTO;
    }

    public int delete(Integer exerciseSetId) {
        int result;
        result = midMapper.deleteByExerciseSetId(exerciseSetId);
        result = setMapper.delete(exerciseSetId);
        return result;
    }

    public ExerciseSetDTO getExerciseSetVMById(Integer exerciseSetId) {
        ExerciseSet exerciseSet = setMapper.getExerciseSetById(exerciseSetId);
        if(exerciseSet.getExerciseList().get(0).getExerciseId() == null){
            exerciseSet.setExerciseList(null);
        }
        ExerciseSetDTO exerciseSetDTO = new ExerciseSetDTO();
        User user = userMapper.getUserById(exerciseSet.getCreateUserId());
        Subject subject = subjectMapper.getSubjectById(exerciseSet.getSubjectId());
        List<Subject> subjectList = subjectMapper.getSubjectByBase(subject.getBaseSubject());
        List<String> base = subjectMapper.getBase();
        BeanUtils.copyProperties(exerciseSet, exerciseSetDTO);
        exerciseSetDTO.setBaseList(base);
        exerciseSetDTO.setSubjectList(subjectList);
        exerciseSetDTO.setUser(user);
        exerciseSetDTO.setSubject(subject);
        return exerciseSetDTO;
    }

    public int countExerciseSet() {
        return setMapper.countExerciseSetList(new Query());
    }
}