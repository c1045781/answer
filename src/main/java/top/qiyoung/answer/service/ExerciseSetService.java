package top.qiyoung.answer.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.qiyoung.answer.mapper.ExerciseMapper;
import top.qiyoung.answer.mapper.ExerciseSetMapper;
import top.qiyoung.answer.mapper.MidMapper;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.ExerciseSet;
import top.qiyoung.answer.model.ExerciseSetVM;
import top.qiyoung.answer.model.User;

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


    @Transactional
    public void addOrUpdate(ExerciseSetVM setVM, User user) {
        ExerciseSet exerciseSet = new ExerciseSet();
        exerciseSet.setCreateUserId(user.getId());
        exerciseSet.setCreateTime(new Date());
        exerciseSet.setModifyTime(new Date());
        List<Integer> exerciseIds = setVM.getExerciseIds();
        List<Integer> ids = exerciseIds;
        exerciseSet.setExerciseCount(ids.size());
        BeanUtils.copyProperties(setVM, exerciseSet);

        List<Exercise> exerciseList = new ArrayList<>();
        for (Integer id : ids) {
            Exercise exercise = exerciseMapper.getExerciseById(id);
            if (exercise != null){
                exerciseList.add(exercise);
            }
        }
        exerciseSet.setExerciseList(exerciseList);
        int exerciseSetId = setMapper.insert(exerciseSet);
        for (Integer exerciseId : exerciseIds) {
            midMapper.insert(exerciseSetId,exerciseId);
        }
    }

    public List<ExerciseSet> getAll() {
        return setMapper.getAll();

    }
}
