package top.qiyoung.answer.service;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.mapper.ExerciseContentMapper;
import top.qiyoung.answer.mapper.ExerciseMapper;
import top.qiyoung.answer.mapper.SubjectMapper;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExerciseService {


    @Resource
    private ExerciseMapper exerciseMapper;
    @Resource
    private ExerciseContentMapper exerciseContentMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private SubjectMapper subjectMapper;

    public void insert(ExerciseEdit edit, User user) {
        ExerciseContent exerciseContent = new ExerciseContent();
        ExerciseContentVM exerciseContentVM = new ExerciseContentVM();
        Exercise exercise = new Exercise();

        BeanUtils.copyProperties(edit, exercise);
        // 制作content JSON
        exerciseContentVM.setCorrect(edit.getCorrect());
        exerciseContentVM.setOptions(edit.getOptions());
        exerciseContentVM.setTitle(edit.getTitle());
        exerciseContent.setContent(JSON.toJSONString(exerciseContentVM));
        exerciseContent.setCreateTime(new Date());
        exerciseContentMapper.insert(exerciseContent);
        // 插入的contentId
        Integer exerciseContentId = exerciseContent.getId();
        exercise.setExerciseContentId(exerciseContentId);

        exercise.setCreateTime(new Date());
        exercise.setModifyTime(new Date());
        if (user.getRole() == 1) {
            exercise.setStatus(1);
        } else {
            exercise.setStatus(0);
        }
        exercise.setCreateUserId(user.getId());
        exerciseMapper.insert(exercise);
    }

    public void update(ExerciseEdit edit) {
        ExerciseContent exerciseContent = new ExerciseContent();
        ExerciseContentVM exerciseContentVM = new ExerciseContentVM();
        Exercise exercise = new Exercise();


        // 制作content JSON
        exerciseContentVM.setCorrect(edit.getCorrect());
        exerciseContentVM.setOptions(edit.getOptions());
        exerciseContentVM.setTitle(edit.getTitle());

        Exercise dbexercise = exerciseMapper.getExerciseById(edit.getId());
        exerciseContent.setId(dbexercise.getExerciseContentId());
        exerciseContent.setContent(JSON.toJSONString(exerciseContentVM));
        exerciseContentMapper.update(exerciseContent);

        BeanUtils.copyProperties(edit, exercise);
        exercise.setId(edit.getId());
        exercise.setCorrect(edit.getCorrect());
        exercise.setModifyTime(new Date());
        exerciseMapper.update(exercise);
    }

    public Pagination getExerciseVMList(Integer currentPage, Integer size, String search, String type, String order) {
        Pagination<ExerciseVM> pagination = new Pagination<>(currentPage, size);
        List<ExerciseVM> VMList = new ArrayList<>();
        Query query = new Query();
        query.setIndex((currentPage - 1) * size);
        query.setSize(size);
        query.setSearch(search);
        query.setType(type);
        query.setOrder(order);
        List<Exercise> exerciseList = exerciseMapper.getExerciseList(query);
        for (Exercise exercise : exerciseList) {
            ExerciseVM exerciseVM = new ExerciseVM();
            User user = userMapper.getUserById(exercise.getCreateUserId());
            Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
            ExerciseContent exerciseContent = exerciseContentMapper.getExerciseContentById(exercise.getExerciseContentId());
            exerciseVM.setExercise(exercise);
            exerciseVM.setExerciseContentVM(JSON.parseObject(exerciseContent.getContent(), ExerciseContentVM.class));
            exerciseVM.setSubject(subject);
            exerciseVM.setUser(user);
            VMList.add(exerciseVM);
        }
        pagination.setDataList(VMList);

        query.setIndex(null);
        query.setSize(null);
        int count = exerciseMapper.countExerciseList(query);
        pagination.setTotalSize(count);
        pagination.setTotalPage((int) Math.ceil((double) pagination.getTotalSize() / (double) size));
        return pagination;
    }

    public void deleteById(Integer id) {
        Exercise exercise = exerciseMapper.getExerciseById(id);
        exerciseContentMapper.deleteById(exercise.getExerciseContentId());
        exerciseMapper.deleteById(id);
    }

    /*private Integer id;
    private String exerciseType;
    private Integer subjectId;
    private String correct;
    private List<Option> options;
    private String title;
    private List<String> answers;*/

    public ExerciseEdit getExerciseEdit(Integer id) {
        ExerciseEdit edit = new ExerciseEdit();
        Exercise exercise = exerciseMapper.getExerciseById(id);
        edit.setCorrect(exercise.getCorrect());
        edit.setId(id);
        edit.setExerciseType(exercise.getExerciseType());
        edit.setSubjectId(exercise.getSubjectId());
        Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
        List<Subject> subjectList = subjectMapper.getSubjectByBase(subject.getBaseSubject());
        List<String> base = subjectMapper.getBase();
        edit.setBaseList(base);
        edit.setSubjectList(subjectList);
        ExerciseContent exerciseContent = exerciseContentMapper.getExerciseContentById(exercise.getExerciseContentId());
        ExerciseContentVM vm = JSON.parseObject(exerciseContent.getContent(), ExerciseContentVM.class);
        edit.setOptions(vm.getOptions());
        List<String> answerList = new ArrayList<>();
        for (Option option : vm.getOptions()) {
            answerList.add(option.getContent());
        }
        edit.setAnswers(answerList);
        edit.setTitle(vm.getTitle());
        return edit;
    }

/*    private Exercise exercise;
    private Subject subject;
    private User user;
    private ExerciseContentVM exerciseContentVM;*/

   /* exercise -> type id
    exerciseCOntent ->title*/

    public Pagination<ExerciseVM> getExerciseBySubjectId(Integer subjectId, Integer currentPage, Integer size, String search, String type, String order) {

        Pagination<ExerciseVM> pagination = new Pagination<>(currentPage, size);
        List<ExerciseVM> VMList = new ArrayList<>();
        Query query = new Query();
        query.setIndex((currentPage - 1) * size);
        query.setSize(size);
        pagination.setSearch(search);
        query.setType(type);
        pagination.setType(type);
        query.setOrder(order);
        List<Exercise> dbexerciseList = exerciseMapper.getExerciseBySubjectId(query, subjectId);
        List<Exercise> exerciseList = new ArrayList<>();
        List<ExerciseContentVM> contentVMList = new ArrayList<>();
        for (Exercise exercise : dbexerciseList) {
            ExerciseContent content = exerciseContentMapper.getExerciseContentById(exercise.getExerciseContentId());
            ExerciseContentVM contentVM = JSON.parseObject(content.getContent(), ExerciseContentVM.class);
            if (contentVM.getTitle().contains(search)) {
                exerciseList.add(exercise);
                contentVMList.add(contentVM);
            }
        }
        int index = query.getIndex();
        if (exerciseList.size() < (index + size)) {
            for (int i = index; i < exerciseList.size(); i++) {
                ExerciseVM exerciseVM = new ExerciseVM();
                exerciseVM.setExercise(exerciseList.get(i));
                exerciseVM.setExerciseContentVM(contentVMList.get(i));
                VMList.add(exerciseVM);
            }
        } else {
            for (int i = index; i < (index + size); i++) {
                ExerciseVM exerciseVM = new ExerciseVM();
                exerciseVM.setExercise(exerciseList.get(i));
                exerciseVM.setExerciseContentVM(contentVMList.get(i));
                VMList.add(exerciseVM);
            }
        }
        pagination.setDataList(VMList);
        pagination.setTotalSize(exerciseList.size());
        pagination.setTotalPage((int) Math.ceil((double) pagination.getTotalSize() / (double) size));
        return pagination;
    }
}
