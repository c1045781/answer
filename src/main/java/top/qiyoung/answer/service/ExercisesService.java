package top.qiyoung.answer.service;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.mapper.ExercisesContentMapper;
import top.qiyoung.answer.mapper.ExercisesMapper;
import top.qiyoung.answer.mapper.SubjectMapper;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExercisesService {

    @Resource
    private ExercisesMapper exercisesMapper;
    @Resource
    private ExercisesContentMapper exercisesContentMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private SubjectMapper subjectMapper;

    public void insert(ExercisesEdit edit, User user) {
        ExercisesContent exercisesContent = new ExercisesContent();
        ExercisesContentVM exercisesContentVM = new ExercisesContentVM();
        Exercises exercises = new Exercises();

        BeanUtils.copyProperties(edit, exercises);
        // 制作content JSON
        exercisesContentVM.setCorrect(edit.getCorrect());
        exercisesContentVM.setOptions(edit.getOptions());
        exercisesContentVM.setTitle(edit.getTitle());
        exercisesContent.setContent(JSON.toJSONString(exercisesContentVM));
        exercisesContent.setCreateTime(new Date());
        exercisesContentMapper.insert(exercisesContent);
        // 插入的contentId
        Integer exercisesContentId = exercisesContent.getId();
        exercises.setExercisesContentId(exercisesContentId);

        exercises.setCreateTime(new Date());
        exercises.setModifyTime(new Date());
        if (user.getRole() == 1) {
            exercises.setStatus(1);
        } else {
            exercises.setStatus(0);
        }
        exercises.setCreateUserId(user.getId());
        exercisesMapper.insert(exercises);
    }

    public void update(ExercisesEdit edit) {
        ExercisesContent exercisesContent = new ExercisesContent();
        ExercisesContentVM exercisesContentVM = new ExercisesContentVM();
        Exercises exercises = new Exercises();


        // 制作content JSON
        exercisesContentVM.setCorrect(edit.getCorrect());
        exercisesContentVM.setOptions(edit.getOptions());
        exercisesContentVM.setTitle(edit.getTitle());

        Exercises dbexercises = exercisesMapper.getExercisesById(edit.getId());
        exercisesContent.setId(dbexercises.getExercisesContentId());
        exercisesContent.setContent(JSON.toJSONString(exercisesContentVM));
        exercisesContentMapper.update(exercisesContent);

        BeanUtils.copyProperties(edit, exercises);
        exercises.setId(edit.getId());
        exercises.setModifyTime(new Date());
        exercisesMapper.update(exercises);
    }

    public Pagination getExercisesVMList(Integer currentPage, Integer size, String search, String type, String order) {
        Pagination<ExercisesVM> pagination = new Pagination<>(currentPage, size);
        List<ExercisesVM> VMList = new ArrayList<>();
        Query query = new Query();
        query.setIndex((currentPage - 1) * size);
        query.setSize(size);
        query.setSearch(search);
        query.setType(type);
        query.setOrder(order);
        List<Exercises> exercisesList = exercisesMapper.getExercisesList(query);
        for (Exercises exercises : exercisesList) {
            ExercisesVM exercisesVM = new ExercisesVM();
            User user = userMapper.getUserById(exercises.getCreateUserId());
            Subject subject = subjectMapper.getSubjectById(exercises.getSubjectId());
            ExercisesContent exercisesContent = exercisesContentMapper.getExercisesContentById(exercises.getExercisesContentId());
            exercisesVM.setExercises(exercises);
            exercisesVM.setExercisesContentVM(JSON.parseObject(exercisesContent.getContent(),ExercisesContentVM.class));
            exercisesVM.setSubject(subject);
            exercisesVM.setUser(user);
            VMList.add(exercisesVM);
        }
        pagination.setDataList(VMList);

        query.setIndex(null);
        query.setSize(null);
        int count = exercisesMapper.countExercisesList(query);
        pagination.setTotalSize(count);
        pagination.setTotalPage((int) Math.ceil((double) pagination.getTotalSize() / (double) size));
        return pagination;
    }

    public void deleteById(Integer id) {
        Exercises exercises = exercisesMapper.getExercisesById(id);
        exercisesContentMapper.deleteById(exercises.getExercisesContentId());
        exercisesMapper.deleteById(id);
    }

    /*private Integer id;
    private String exercisesType;
    private Integer subjectId;
    private String correct;
    private List<Option> options;
    private String title;
    private List<String> answers;*/

    public ExercisesEdit getExercisesEdit(Integer id) {
        ExercisesEdit edit = new ExercisesEdit();
        Exercises exercises = exercisesMapper.getExercisesById(id);
        edit.setCorrect(exercises.getCorrect());
        edit.setId(id);
        edit.setExercisesType(exercises.getExercisesType());
        edit.setSubjectId(exercises.getSubjectId());
        Subject subject = subjectMapper.getSubjectById(exercises.getSubjectId());
        List<Subject> subjectList = subjectMapper.getSubjectByBase(subject.getBaseSubject());
        List<String> base = subjectMapper.getBase();
        edit.setBaseList(base);
        edit.setSubjectList(subjectList);
        ExercisesContent exercisesContent = exercisesContentMapper.getExercisesContentById(exercises.getExercisesContentId());
        ExercisesContentVM vm = JSON.parseObject(exercisesContent.getContent(), ExercisesContentVM.class);
        edit.setOptions(vm.getOptions());
        List<String> answerList = new ArrayList<>();
        for (Option option : vm.getOptions()) {
            answerList.add(option.getContent());
        }
        edit.setAnswers(answerList);
        edit.setTitle(vm.getTitle());
        return edit;
    }


}
