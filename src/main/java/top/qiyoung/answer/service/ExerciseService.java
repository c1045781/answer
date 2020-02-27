package top.qiyoung.answer.service;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.DTO.ExerciseEditDTO;
import top.qiyoung.answer.DTO.ExerciseDTO;
import top.qiyoung.answer.DTO.PaginationDTO;
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

    public int insert(ExerciseEditDTO edit, User user) {
        Exercise exercise = new Exercise(null,edit.getExerciseType(),edit.getCorrect(),edit.getTitle(),JSON.toJSONString(edit.getOptions()),user.getUserId(),
                null,new Date(),new Date(),edit.getSubjectId(),edit.getAnalysis());

        // 插入的option
        /*exercise.setExerciseType(edit.getExerciseType());
        exercise.setSubjectId(edit.getSubjectId());
        exercise.setOptionContent(JSON.toJSONString(edit.getOptions()));
        exercise.setCorrect(edit.getCorrect());
        exercise.setExerciseTitle(edit.getTitle());
        exercise.setCreateUserId(user.getUserId());
        exercise.setCreateTime(new Date());
        exercise.setModifyTime(new Date());*/
        if (user.getRole() == 1) {
            exercise.setStatus(1);
        } else {
            exercise.setStatus(0);
        }
        return exerciseMapper.insert(exercise);
    }

    public int update(ExerciseEditDTO edit) {
        Exercise exercise = new Exercise(edit.getExerciseEditId(),edit.getExerciseType(),edit.getCorrect(),edit.getTitle(),JSON.toJSONString(edit.getOptions()),null,
                null,null,new Date(),null,edit.getAnalysis());

        //  content JSON
        /*exercise.setOptionContent(JSON.toJSONString(edit.getOptions()));
        exercise.setExerciseId(edit.getExerciseEditId());
        exercise.setExerciseTitle(edit.getTitle());
        exercise.setExerciseType(edit.getExerciseType());
        exercise.setCorrect(edit.getCorrect());
        exercise.setModifyTime(new Date());*/
        return exerciseMapper.update(exercise);
    }

    public PaginationDTO<ExerciseDTO> getExerciseList(Integer currentPage, Integer size, String search,
                                                      String type, String orderby, Integer subjectId, String exerciseType) {
        List<Exercise> exercises = new ArrayList<>();
        int count = 0;
        PaginationDTO<ExerciseDTO> paginationDTO = new PaginationDTO<>(currentPage, size);
        Query query = new Query();
        query.setIndex((currentPage - 1) * size);
        query.setSize(size);
        query.setType(type);
        query.setExerciseType(exerciseType);
        query.setOrder(orderby);
        query.setId(subjectId);
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
        List<ExerciseDTO> exerciseDTOS = exerciseToExerciseDTO(exercises);
        paginationDTO.setCurrentPage(currentPage);
        paginationDTO.setPageSize(size);
        paginationDTO.setDataList(exerciseDTOS);
        paginationDTO.setTotalSize(count);
        paginationDTO.setTotalPage((int) Math.ceil((double) paginationDTO.getTotalSize() / (double) size));
        return paginationDTO;
    }

    public int deleteById(Integer id) {
        int result;
        result =  midMapper.deleteByExerciseId(id);
        result = exerciseMapper.deleteById(id);
        return result;
    }

    public ExerciseEditDTO getExerciseEdit(Integer id) {

        Exercise exercise = exerciseMapper.getExerciseById(id);
        Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
        List<Subject> subjectList = subjectMapper.getSubjectByBase(subject.getBaseSubject());
        List<String> base = subjectMapper.getBase();
        List<Option> options = JSON.parseArray(exercise.getOptionContent(), Option.class);
        List<String> answerList = new ArrayList<>();

        for (Option option : options) {
            answerList.add(option.getContent());

        }
        ExerciseEditDTO edit = new ExerciseEditDTO(id,exercise.getExerciseType(),base,exercise.getSubjectId(),subjectList,
                exercise.getCorrect(),options,exercise.getExerciseTitle(),answerList,exercise.getAnalysis());
       /* edit.setExerciseEditId(id);
        edit.setExerciseType(exercise.getExerciseType());
        edit.setBaseList(base);
        edit.setSubjectList(subjectList);
        edit.setSubjectId(exercise.getSubjectId());
        edit.setCorrect(exercise.getCorrect());
        edit.setOptions(options);
        edit.setTitle(exercise.getExerciseTitle());
        edit.setAnswers(answerList);*/
        return edit;
    }




    public PaginationDTO<ExerciseDTO> getExerciseBySubjectId(Integer subjectId, Integer currentPage, Integer size, String search, String type, String order) {

        PaginationDTO<ExerciseDTO> paginationDTO = new PaginationDTO<>(currentPage, size);
        List<ExerciseDTO> exerciseDTOS = new ArrayList<>();
        Query query = new Query();
        query.setIndex((currentPage - 1) * size);
        query.setSize(size);
        paginationDTO.setSearch(search);
        query.setType(type);
        paginationDTO.setType(type);
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
            ExerciseDTO exerciseDTO = new ExerciseDTO();
            exerciseDTO.setExercise(exerciseList.get(i));
            List<Option> options = JSON.parseArray(exerciseList.get(i).getOptionContent(), Option.class);
            exerciseDTO.setOptions(options);
            exerciseDTOS.add(exerciseDTO);
        }
        paginationDTO.setDataList(exerciseDTOS);
        paginationDTO.setTotalSize(exerciseList.size());
        paginationDTO.setTotalPage((int) Math.ceil((double) paginationDTO.getTotalSize() / (double) size));
        return paginationDTO;
    }

    public List<Exercise> getExerciseListBySubjectId(Integer subjectId) {
        return exerciseMapper.getExerciseListBySubjectId(subjectId);
    }

    public PaginationDTO<ExerciseDTO> review(Integer currentPage, Integer size, Integer subjectId, String order) {
        PaginationDTO<ExerciseDTO> paginationDTO = new PaginationDTO<>(currentPage,size);
        Query query = new Query();
        query.setIndex((currentPage-1)*size);
        query.setSize(size);
        query.setOrder(order);
        query.setId(subjectId);
        List<Exercise> exerciseList = exerciseMapper.getExerciseByReview(query);
        query.setIndex(null);
        query.setSize(null);
        int count = exerciseMapper.countExerciseByReview(query);

        List<ExerciseDTO> exerciseDTOS = exerciseToExerciseDTO(exerciseList);
        paginationDTO.setDataList(exerciseDTOS);
        paginationDTO.setTotalSize(count);
        paginationDTO.setTotalPage((int) Math.ceil((double) paginationDTO.getTotalSize() / (double) size));
        return paginationDTO;
    }

    private List<ExerciseDTO> exerciseToExerciseDTO(List<Exercise> exerciseList) {
        List<ExerciseDTO> exerciseDTOS = new ArrayList<>();
        for (Exercise exercise : exerciseList) {
            ExerciseDTO exerciseDTO = new ExerciseDTO();
            User user = userMapper.getUserById(exercise.getCreateUserId());
            Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
            exerciseDTO.setExercise(exercise);
            exerciseDTO.setOptions(JSON.parseArray(exercise.getOptionContent(), Option.class));
            exerciseDTO.setSubject(subject);
            exerciseDTO.setUser(user);
            exerciseDTOS.add(exerciseDTO);
        }
        return exerciseDTOS;
    }

    public ExerciseDTO getExerciseDTOById(Integer id) {
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        Exercise exercise = exerciseMapper.getExerciseById(id);
        User user = userMapper.getUserById(exercise.getCreateUserId());
        Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
        exerciseDTO.setExercise(exercise);
        exerciseDTO.setOptions(JSON.parseArray(exercise.getOptionContent(), Option.class));
        exerciseDTO.setSubject(subject);
        exerciseDTO.setUser(user);
        return exerciseDTO;
    }

    public int updateSatus(Integer id, Integer status) {
        return exerciseMapper.updateById(id,status);
    }

    public int countExercise() {
        return exerciseMapper.countExerciseList(new Query());
    }

    public List<Integer> getExerciseIdListByExerciseSetId(Integer exerciseSetId) {
        return midMapper.getExerciseIdListByExerciseSetId(exerciseSetId);
    }
}
