package top.qiyoung.answer.service;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.mapper.ExerciseMapper;
import top.qiyoung.answer.mapper.MidMapper;
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
    private UserMapper userMapper;
    @Resource
    private SubjectMapper subjectMapper;
    @Resource
    private MidMapper midMapper;

    public void insert(ExerciseEdit edit, User user) {
        Exercise exercise = new Exercise();


        // 插入的option
        exercise.setExerciseType(edit.getExerciseType());
        exercise.setSubjectId(edit.getSubjectId());
        exercise.setOptionContent(JSON.toJSONString(edit.getOptions()));
        exercise.setCorrect(edit.getCorrect());
        exercise.setExerciseTitle(edit.getTitle());
        exercise.setCreateUserId(user.getUserId());
        exercise.setCreateTime(new Date());
        exercise.setModifyTime(new Date());
        if (user.getRole() == 1) {
            exercise.setStatus(1);
        } else {
            exercise.setStatus(0);
        }


        exerciseMapper.insert(exercise);
    }

    public void update(ExerciseEdit edit) {
        Exercise exercise = new Exercise();


        // 制作content JSON
        exercise.setOptionContent(JSON.toJSONString(edit.getOptions()));
        exercise.setExerciseId(edit.getExerciseEditId());
        exercise.setExerciseTitle(edit.getTitle());
        exercise.setExerciseType(edit.getExerciseType());
        exercise.setCorrect(edit.getCorrect());
        exercise.setModifyTime(new Date());
        exerciseMapper.update(exercise);
    }

    public Pagination<ExerciseVM> getExerciseList(Integer currentPage, Integer size,
                                                  String search, String type, String order, Integer subjectId,String exerciseType) {
        List<Exercise> exercises = new ArrayList<>();
        int count = 0;
        Pagination<ExerciseVM> pagination = new Pagination<>(currentPage, size);
        List<ExerciseVM> VMList = new ArrayList<>();
        Query query = new Query();
        query.setIndex((currentPage - 1) * size);
        query.setSize(size);
        query.setType(type);
        query.setExerciseType(exerciseType);
        query.setOrder(order);
        query.setSubjectId(subjectId);
        if (type != null && type.equals("createUser")) {
            List<User> users = userMapper.getUserByUsername(search);
            for (User dbuser : users) {
                query.setIndex((currentPage - 1) * size);
                query.setSize(size);
                query.setSearch(dbuser.getUserId()+"");
                List<Exercise> exerciseList = exerciseMapper.getExerciseList(query);
                for (Exercise exercise : exerciseList) {
                    exercises.add(exercise);
                }
                query.setIndex(null);
                query.setSize(null);
                count += exerciseMapper.countExerciseList(query);
            }
        } else {
            query.setSearch(search);
            exercises = exerciseMapper.getExerciseList(query);
            query.setIndex(null);
            query.setSize(null);
            count = exerciseMapper.countExerciseList(query);

        }
        for (Exercise exercise : exercises) {
            ExerciseVM exerciseVM = new ExerciseVM();
            User user = userMapper.getUserById(exercise.getCreateUserId());
            Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
            exerciseVM.setExercise(exercise);
            exerciseVM.setOptions(JSON.parseArray(exercise.getOptionContent(), Option.class));
            exerciseVM.setSubject(subject);
            exerciseVM.setUser(user);
            VMList.add(exerciseVM);
        }
        pagination.setCurrentPage(currentPage);
        pagination.setPageSize(size);
        pagination.setDataList(VMList);
        pagination.setTotalSize(count);
        pagination.setTotalPage((int) Math.ceil((double) pagination.getTotalSize() / (double) size));
        return pagination;
    }

    public void deleteById(Integer id) {
        midMapper.deleteByExerciseId(id);
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
        Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
        List<Subject> subjectList = subjectMapper.getSubjectByBase(subject.getBaseSubject());
        List<String> base = subjectMapper.getBase();
        List<Option> options = JSON.parseArray(exercise.getOptionContent(), Option.class);
        List<String> answerList = new ArrayList<>();

        for (Option option : options) {
            answerList.add(option.getContent());

        }
        edit.setExerciseEditId(id);
        edit.setExerciseType(exercise.getExerciseType());
        edit.setBaseList(base);
        edit.setSubjectList(subjectList);
        edit.setSubjectId(exercise.getSubjectId());
        edit.setCorrect(exercise.getCorrect());
        edit.setOptions(options);
        edit.setTitle(exercise.getExerciseTitle());
        edit.setAnswers(answerList);
        return edit;
    }

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
        for (Exercise exercise : dbexerciseList) {
            if (exercise.getExerciseTitle().contains(search)) {
                exerciseList.add(exercise);
            }
        }
        int index = query.getIndex();
        int length;
        if (exerciseList.size() < (index + size)) {
            length = exerciseList.size();
        } else {
            length = index + size;
        }
        for (int i = index; i < length; i++) {
            ExerciseVM exerciseVM = new ExerciseVM();
            exerciseVM.setExercise(exerciseList.get(i));
            List<Option> options = JSON.parseArray(exerciseList.get(i).getOptionContent(), Option.class);
            exerciseVM.setOptions(options);
            VMList.add(exerciseVM);
        }
        pagination.setDataList(VMList);
        pagination.setTotalSize(exerciseList.size());
        pagination.setTotalPage((int) Math.ceil((double) pagination.getTotalSize() / (double) size));
        return pagination;
    }
}
