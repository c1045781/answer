package top.qiyoung.answer.service;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.DTO.*;
import top.qiyoung.answer.mapper.*;
import top.qiyoung.answer.model.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

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
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private EvaluationMapper evaluationMapper;
    @Resource
    private AnswerMapper answerMapper;
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private NoteMapper noteMapper;

    public int insert(ExerciseEditDTO edit, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        Exercise exercise = new Exercise(null,edit.getExerciseType(),edit.getCorrect(),edit.getTitle(),JSON.toJSONString(edit.getOptions()), myUser.getUserId(),
                null,new Date(),new Date(),edit.getSubjectId(),edit.getAnalysis(),0d,0,0);

        // 插入的option
        /*exercise.setExerciseType(edit.getExerciseType());
        exercise.setSubjectId(edit.getSubjectId());
        exercise.setOptionContent(JSON.toJSONString(edit.getOptions()));
        exercise.setCorrect(edit.getCorrect());
        exercise.setExerciseTitle(edit.getTitle());
        exercise.setCreateUserId(myUser.getUserId());
        exercise.setCreateTime(new Date());
        exercise.setModifyTime(new Date());*/
        int id;
        if (myUser.getRole() == 1 || myUser.getRole() == 0) {
            exercise.setStatus(1);
            id = exerciseMapper.insert(exercise);
        } else {
            exercise.setStatus(0);
            id = exerciseMapper.insert(exercise);
            Message message = new Message(null,exercise.getExerciseId(),null,2,null, myUser.getUserId(),new Date(),0);
            permissionMapper.add(message);
        }
        return id;
    }

    public int update(ExerciseEditDTO edit, UserDetails userDetails ) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        Exercise exercise;
        if (myUser.getRole() == 1 || myUser.getRole() == 0) {
            exercise = new Exercise(edit.getExerciseEditId(),edit.getExerciseType(),edit.getCorrect(),edit.getTitle(),JSON.toJSONString(edit.getOptions()),null,
                    null,null,new Date(),null,edit.getAnalysis());
        }else {
            exercise = new Exercise(edit.getExerciseEditId(),edit.getExerciseType(),edit.getCorrect(),edit.getTitle(),JSON.toJSONString(edit.getOptions()),null,
                    0,null,new Date(),null,edit.getAnalysis());
            permissionMapper.updateStatus(permissionMapper.getMessageByExerciseIdAndUserId(exercise.getExerciseId(), myUser.getUserId()).getMessageId(),"",0);
        }


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
                                                      String type, String orderby, Integer subjectId, String exerciseType,Integer score) {
        List<Exercise> exercises = new ArrayList<>();
        int count = 0;
        PaginationDTO<ExerciseDTO> paginationDTO = new PaginationDTO<>(currentPage, size);
       /* Query query = new Query();
        query.setIndex((currentPage - 1) * size);
        query.setSize(size);
        query.setType(type);
        query.setExerciseType(exerciseType);
        query.setOrder(orderby);
        query.setId(subjectId);*/
        int index = (currentPage - 1) * size;
        if (type != null && type.equals("createUser")) {
            List<MyUser> myUsers = userMapper.getUserByNickname(search);
            for (MyUser dbuser : myUsers) {

                search = dbuser.getUserId() + "";
               /* query.setIndex(index);
                query.setSize(size);
                query.setSearch(search);*/
                List<Exercise> exerciseList = exerciseMapper.getExerciseList(index,size,search,type,exerciseType,orderby,subjectId,score);
                for (Exercise exercise : exerciseList) {
                    exercises.add(exercise);
                }
                /*query.setIndex(null);
                query.setSize(null);*/
                count += exerciseMapper.countExerciseList(search,type,exerciseType,orderby,subjectId,score);
            }
        } else {
            exercises = exerciseMapper.getExerciseList(index,size,search,type,exerciseType,orderby,subjectId,score);
            /*query.setSearch(search);
            query.setIndex(null);
            query.setSize(null);*/
            count = exerciseMapper.countExerciseList(search,type,exerciseType,orderby,subjectId,score);
        }
        List<ExerciseDTO> exerciseDTOS = exerciseToExerciseDTO(exercises);
        paginationDTO.setCurrentPage(currentPage);
        paginationDTO.setPageSize(size);
        paginationDTO.setDataList(exerciseDTOS);
        paginationDTO.setTotalSize(count);
        paginationDTO.setTotalPage((int) Math.ceil((double) paginationDTO.getTotalSize() / (double) size));
        return paginationDTO;
    }

    public void deleteByExerciseId(Integer exerciseId) {
        // 删除外键数据
        collectMapper.deleteCollectByExerciseId(exerciseId);
        permissionMapper.deleteByExerciseId(exerciseId);
        evaluationMapper.deleteByExerciseId(exerciseId);
        commentMapper.deleteByExerciseId(exerciseId);
        answerMapper.deleteByExerciseId(exerciseId);
        midMapper.deleteByExerciseId(exerciseId);
        noteMapper.deleteByExerciseId(exerciseId);

        exerciseMapper.deleteByExerciseId(exerciseId);
    }

    public ExerciseEditDTO getExerciseEdit(Integer exerciseId) {
        Exercise exercise = exerciseMapper.getExerciseByExerciseId(exerciseId);
        Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
        List<Subject> subjectList = subjectMapper.getSubjectByBase(subject.getBaseSubject());
        List<String> base = subjectMapper.getBase();
        List<Option> options = JSON.parseArray(exercise.getOptionContent(), Option.class);
        List<String> answerList = new ArrayList<>();

        for (Option option : options) {
            answerList.add(option.getContent());

        }
        ExerciseEditDTO edit = new ExerciseEditDTO(exerciseId,exercise.getExerciseType(),base,exercise.getSubjectId(),subjectList,
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

    public PaginationDTO<ExerciseReviewDTO> review(Integer currentPage, Integer size, Integer subjectId, String order) {
        PaginationDTO<ExerciseReviewDTO> paginationDTO = new PaginationDTO<>(currentPage,size);
        Query query = new Query();
        query.setIndex((currentPage-1)*size);
        query.setSize(size);
        query.setOrder(order);
        query.setId(subjectId);

        List<EIdAndMId> eIdAndMIds = exerciseMapper.getReviewExercise(query);
        List<ExerciseReviewDTO> list = new ArrayList<>();
        for (EIdAndMId eIdAndMId : eIdAndMIds) {
            Exercise exercise = exerciseMapper.getExerciseByExerciseId(eIdAndMId.getExerciseId());
            Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
            Message message = permissionMapper.getMessageByMessageId(eIdAndMId.getMessageId());
            MyUser myUser = userMapper.getUserById(exercise.getCreateUserId());
            ExerciseReviewDTO dto = new ExerciseReviewDTO(exercise,message,subject, myUser,JSON.parseArray(exercise.getOptionContent(), Option.class));
            list.add(dto);
        }
        query.setIndex(null);
        query.setSize(null);
        int count = exerciseMapper.countReviewExercise(query);

        paginationDTO.setDataList(list);
        paginationDTO.setTotalSize(count);
        paginationDTO.setTotalPage((int) Math.ceil((double) paginationDTO.getTotalSize() / (double) size));
        return paginationDTO;
    }

    private List<ExerciseDTO> exerciseToExerciseDTO(List<Exercise> exerciseList) {
        List<ExerciseDTO> exerciseDTOS = new ArrayList<>();
        for (Exercise exercise : exerciseList) {
            ExerciseDTO exerciseDTO = new ExerciseDTO();
            MyUser myUser = userMapper.getUserById(exercise.getCreateUserId());
            Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
            exerciseDTO.setExercise(exercise);
            exerciseDTO.setOptions(JSON.parseArray(exercise.getOptionContent(), Option.class));
            exerciseDTO.setSubject(subject);
            exerciseDTO.setMyUser(myUser);
            exerciseDTOS.add(exerciseDTO);
        }
        return exerciseDTOS;
    }

    public ExerciseDTO getExerciseDTOByExerciseId(Integer exerciseId) {
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        Exercise exercise = exerciseMapper.getExerciseByExerciseId(exerciseId);
        MyUser myUser = userMapper.getUserById(exercise.getCreateUserId());
        Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
        exerciseDTO.setExercise(exercise);
        exerciseDTO.setOptions(JSON.parseArray(exercise.getOptionContent(), Option.class));
        exerciseDTO.setSubject(subject);
        exerciseDTO.setMyUser(myUser);
        return exerciseDTO;
    }

    public void updateSatus(Integer id, Integer status, String reason, Integer messageId) {
        permissionMapper.updateStatus(messageId,reason,status);
        exerciseMapper.updateById(id,status);
    }

    public int countExercise() {
        return exerciseMapper.countExerciseList(null,null,null,null,null,null);
    }

    public List<Integer> getExerciseIdListByExerciseSetId(Integer exerciseSetId) {
        return midMapper.getExerciseIdListByExerciseSetId(exerciseSetId);
    }

    public PaginationDTO<Exercise> getReviewExerciseByUserId(MyUser myUser, Integer currentPage, Integer size, Integer status) {
//        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        List<Exercise> exerciseList = exerciseMapper.getReviewExerciseByUserId(myUser.getUserId(),(currentPage-1)*size,size,status);
        int count = exerciseMapper.countReviewExerciseByUserId(myUser.getUserId(),status);
        PaginationDTO dto = new PaginationDTO(currentPage,size,(int)Math.ceil((double)count/(double)size),count,null,""+status,exerciseList);
        return dto;
    }

    public ExerciseReviewDTO checkOfAddExercise(Integer exerciseId, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        Exercise exercise = exerciseMapper.getExerciseByExerciseId(exerciseId);
        Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
        Message message = permissionMapper.getMessageByExerciseIdAndUserId(exerciseId, myUser.getUserId());
        ExerciseReviewDTO exerciseReviewDTO = new ExerciseReviewDTO(exercise,message,subject, myUser,JSON.parseArray(exercise.getOptionContent(), Option.class));
        return exerciseReviewDTO;
    }

    public void deleteByUserId(Integer userId) {
        List<Integer> exerciseIds = exerciseMapper.findExerciseIdListByUserId(userId);
        for (Integer exerciseId : exerciseIds) {
            deleteByExerciseId(exerciseId);
        }
    }

    public List<Exercise> getHotExercise(Integer subjectId) {
        List<Exercise> exerciseList = exerciseMapper.getHotExercise(subjectId);
        Map<Exercise,Integer> map = new LinkedHashMap<>();
        for (Exercise exercise : exerciseList) {
            int count = commentMapper.countByExerciseId(exercise.getExerciseId());
            map.put(exercise,count*10+exercise.getDoCount());
        }
        Map<Exercise,Integer> reslut = new LinkedHashMap<>();
        Stream<Map.Entry<Exercise, Integer>> stream = map.entrySet().stream();
        stream.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEach(e ->reslut.put(e.getKey(),e.getValue()));
        List<Map.Entry<Exercise,Integer>> list = new ArrayList<>(reslut.entrySet());
        exerciseList.clear();
        if (list.size()>10){
            for (Map.Entry<Exercise, Integer> entry : list.subList(0,10)) {
                exerciseList.add(entry.getKey());
            }
        }else {
            for (Map.Entry<Exercise, Integer> entry : list) {
                exerciseList.add(entry.getKey());
            }
        }

        return exerciseList;

    }

    public List<Exercise> getExcellentExercise(Integer subjectId) {
        List<Exercise> exerciseList = exerciseMapper.getExcellentExercise(subjectId);
        return exerciseList;
    }
}
