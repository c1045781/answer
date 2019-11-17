package top.qiyoung.answer.service;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
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

    public void insert(ExerciseSetVM setVM, User user) {
        ExerciseSet exerciseSet = new ExerciseSet();

        exerciseSet.setCreateUserId(user.getUserId());
        exerciseSet.setCreateTime(new Date());
        exerciseSet.setModifyTime(new Date());
        List<Integer> exerciseIds = new ArrayList<>();
        for (Exercise exercise : setVM.getExerciseList()) {
            exerciseIds.add(exercise.getExerciseId());
        }
        exerciseSet.setExerciseCount(exerciseIds.size());
        exerciseSet.setSubjectId(setVM.getSubject().getSubjectId());
        exerciseSet.setTitle(setVM.getTitle());
        /*List<Exercise> exerciseList = new ArrayList<>();
        for (Integer id : exerciseIds) {
            Exercise exercise = exerciseMapper.getExerciseById(id);
            if (exercise != null){
                exerciseList.add(exercise);
            }
        }
        exerciseSet.setExerciseList(exerciseList);*/
        int exerciseSetId = 1;
        Integer lastId = setMapper.getLastId();
        if (lastId != null ){
            exerciseSetId = lastId + 1;
        }
        exerciseSet.setExerciseSetId(exerciseSetId);
        setMapper.insert(exerciseSet);
        for (Integer exerciseId : exerciseIds) {
            midMapper.insert(exerciseSetId,exerciseId);
        }
    }

    public void update(ExerciseSetVM setVM) {
        midMapper.deleteByExerciseSetId(setVM.getExerciseSetId());

        ExerciseSet exerciseSet = new ExerciseSet();

        List<Integer> exerciseIds = new ArrayList<>();
        for (Exercise exercise : setVM.getExerciseList()) {
            exerciseIds.add(exercise.getExerciseId());
        }

        exerciseSet.setExerciseSetId(setVM.getExerciseSetId());
        exerciseSet.setTitle(setVM.getTitle());
        exerciseSet.setSubjectId(setVM.getSubject().getSubjectId());
        exerciseSet.setExerciseCount(exerciseIds.size());
        exerciseSet.setModifyTime(new Date());

        setMapper.update(exerciseSet);
        for (Integer exerciseId : exerciseIds) {
            midMapper.insert(setVM.getExerciseSetId(),exerciseId);
        }
//        Exercise exercise = new Exercise();
//
//
//        // 制作content JSON
//        exercise.setOptionContent(JSON.toJSONString(edit.getOptions()));
//        exercise.setExerciseId(edit.getExerciseEditId());
//        exercise.setExerciseTitle(edit.getTitle());
//        exercise.setExerciseType(edit.getExerciseType());
//        exercise.setCorrect(edit.getCorrect());
//        exercise.setModifyTime(new Date());
//        exerciseMapper.update(exercise);

    }

    public Pagination<ExerciseSetVM> getAll(Integer currentPage, Integer size, String title, Integer subjectId,Integer userId,String order) {
        Pagination<ExerciseSetVM> pagination = new Pagination<>(currentPage, size);
        Query query = new Query();
        query.setIndex((currentPage - 1) * size);
        query.setSize(size);
        query.setOrder(order);
        List<ExerciseSet> exerciseList = setMapper.getExerciseSetList(query,title,subjectId,userId);
        List<ExerciseSetVM> vms = new ArrayList<>();
        for (ExerciseSet set : exerciseList) {
            ExerciseSetVM exerciseSetVM = new ExerciseSetVM();
            Subject subject = subjectMapper.getSubjectById(set.getSubjectId());
            User user = userMapper.getUserById(set.getCreateUserId());
            BeanUtils.copyProperties(set,exerciseSetVM);
            exerciseSetVM.setUser(user);
            exerciseSetVM.setSubject(subject);
            vms.add(exerciseSetVM);
        }
        query.setIndex(null);
        query.setSize(null);
        int count = setMapper.countExerciseSetList(query,title,subjectId,userId);

        pagination.setDataList(vms);
        pagination.setTotalSize(count);
        pagination.setTotalPage((int) Math.ceil((double) pagination.getTotalSize() / (double) size));
        return pagination;

    }

    public void delete(Integer exerciseSetId) {

        setMapper.delete(exerciseSetId);
    }

    public ExerciseSetVM getExerciseSetVMById(Integer exerciseSetId) {
        ExerciseSet exerciseSet = setMapper.getExerciseSetById(exerciseSetId);
        ExerciseSetVM exerciseSetVM = new ExerciseSetVM();
        User user = userMapper.getUserById(exerciseSet.getCreateUserId());
        Subject subject = subjectMapper.getSubjectById(exerciseSet.getSubjectId());
        List<Subject> subjectList = subjectMapper.getSubjectByBase(subject.getBaseSubject());
        List<String> base = subjectMapper.getBase();
        BeanUtils.copyProperties(exerciseSet,exerciseSetVM);
        exerciseSetVM.setBaseList(base);
        exerciseSetVM.setSubjectList(subjectList);
        exerciseSetVM.setUser(user);
        exerciseSetVM.setSubject(subject);
//        exerciseSetVM.setExerciseList(exerciseSet.getExerciseList());
//        exerciseSetVM.setExerciseSetId(exerciseSet.getExerciseSetId());
//        exerciseSetVM.setTitle(exerciseSet.getTitle());
        return exerciseSetVM;
    }


}
