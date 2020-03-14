package top.qiyoung.answer.service;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.DTO.ExerciseReviewDTO;
import top.qiyoung.answer.DTO.ExerciseSetAndExercisesDTO;
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

    public int insert(ExerciseSetDTO setVM, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        int result;
        ExerciseSet exerciseSet = new ExerciseSet();

        exerciseSet.setCreateUserId(myUser.getUserId());
        exerciseSet.setLikeCount(0);
        exerciseSet.setCreateTime(new Date());
        exerciseSet.setModifyTime(new Date());
        List<Integer> exerciseIds = new ArrayList<>();
        if ( setVM.getExerciseList() != null){
            for (Exercise exercise : setVM.getExerciseList()) {
                exerciseIds.add(exercise.getExerciseId());
            }
        }
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
            List<MyUser> myUsers = userMapper.getUserByNickname(search);
            List<ExerciseSet> exerciseList = new ArrayList<>();
            for (MyUser dbuser : myUsers) {
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
            MyUser myUser = userMapper.getUserById(exerciseSet.getCreateUserId());
            BeanUtils.copyProperties(exerciseSet, exerciseSetDTO);
            exerciseSetDTO.setMyUser(myUser);
            exerciseSetDTO.setSubject(subject);
            vms.add(exerciseSetDTO);
        }
        paginationDTO.setDataList(vms);
        paginationDTO.setTotalSize(count);
        paginationDTO.setTotalPage((int) Math.ceil((double) paginationDTO.getTotalSize() / (double) size));
        return paginationDTO;
    }

    public void delete(Integer exerciseSetId) {
        midMapper.deleteByExerciseSetId(exerciseSetId);
        setMapper.delete(exerciseSetId);
    }

    public ExerciseSetDTO getExerciseSetVMById(Integer exerciseSetId) {
        ExerciseSet exerciseSet = setMapper.getExerciseSetById(exerciseSetId);
        if(exerciseSet.getExerciseList().get(0).getExerciseId() == null){
            exerciseSet.setExerciseList(null);
        }
        ExerciseSetDTO exerciseSetDTO = new ExerciseSetDTO();
        MyUser myUser = userMapper.getUserById(exerciseSet.getCreateUserId());
        Subject subject = subjectMapper.getSubjectById(exerciseSet.getSubjectId());
        List<Subject> subjectList = subjectMapper.getSubjectByBase(subject.getBaseSubject());
        List<String> base = subjectMapper.getBase();
        BeanUtils.copyProperties(exerciseSet, exerciseSetDTO);
        exerciseSetDTO.setBaseList(base);
        exerciseSetDTO.setSubjectList(subjectList);
        exerciseSetDTO.setMyUser(myUser);
        exerciseSetDTO.setSubject(subject);
        return exerciseSetDTO;
    }

    public int countExerciseSet() {
        return setMapper.countExerciseSetList(new Query());
    }

    public PaginationDTO<ExerciseSetDTO> getExerciseListByUserId(Integer currentPage, Integer size, String orderBy, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        List<ExerciseSet> exerciseSets = setMapper.getExerciseSetListByUserId((currentPage-1)*size,size,orderBy, myUser.getUserId());
        int count = setMapper.countExerciseSetListByUserId(myUser.getUserId());
        List<ExerciseSetDTO> exerciseSetDTOS = new ArrayList<>();
        for (ExerciseSet exerciseSet : exerciseSets) {
            Subject subject = subjectMapper.getSubjectById(exerciseSet.getSubjectId());
            List<Exercise> list = midMapper.getExerciseListByExerciseSetId(exerciseSet.getExerciseSetId());
            ExerciseSetDTO exerciseSetDTO = new ExerciseSetDTO(exerciseSet.getExerciseSetId(),exerciseSet.getTitle(),exerciseSet.getCreateTime(),subject,null,null,list, myUser,exerciseSet.getLikeCount());
            exerciseSetDTOS.add(exerciseSetDTO);
        }

        PaginationDTO<ExerciseSetDTO> paginationDTO = new PaginationDTO(currentPage,size,(int)Math.ceil((double)count/(double)size),count,null,null,exerciseSetDTOS);
        return paginationDTO;
    }

    public PaginationDTO<ExerciseSetAndExercisesDTO> checkOfExerciseSet(Integer exerciseSetId, Integer currentPage, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        List<Integer> exerciseIdList = midMapper.getExerciseIdListByExerciseSetId(exerciseSetId);
        ExerciseSet set = setMapper.getExerciseSetById(exerciseSetId);
        List<ExerciseReviewDTO> exerciseReviewDTOS = new ArrayList<>();
        int end;
        if (exerciseIdList.size()>currentPage*2){
            end = currentPage*2;
        }else {
            end = exerciseIdList.size();
        }
        for (int i=(currentPage-1)*2;i<end;i++){
            Exercise exercise = exerciseMapper.getExerciseByExerciseId(exerciseIdList.get(i));
            Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
            ExerciseReviewDTO exerciseReviewDTO = new ExerciseReviewDTO(exercise,null,subject, myUser, JSON.parseArray(exercise.getOptionContent(), Option.class));
            exerciseReviewDTOS.add(exerciseReviewDTO);
        }
        ExerciseSetAndExercisesDTO dto = new ExerciseSetAndExercisesDTO(set,exerciseReviewDTOS);
        List<ExerciseSetAndExercisesDTO> list = new ArrayList<>();
        list.add(dto);
        PaginationDTO<ExerciseSetAndExercisesDTO> paginationDTO = new PaginationDTO<>(currentPage,2,(int)Math.ceil((double)exerciseIdList.size()/(double)2),
                exerciseIdList.size(),null,null,list);
        return paginationDTO;
    }

    public void deleteByUserId(Integer userId) {
        List<Integer> exerciseSetIds = setMapper.findExerciseSetIdListByUser(userId);
        for (Integer exerciseSetId : exerciseSetIds) {
            delete(exerciseSetId);
        }
    }

    public void addLike(Integer exerciseSetId) {
        setMapper.addLike(exerciseSetId);
    }

    public void delLike(Integer exerciseSetId) {
        setMapper.delLike(exerciseSetId);
    }

    public List<ExerciseSetDTO> getHighLikeExerciseSet(Integer subjectId) {
        List<ExerciseSet> exerciseSets = setMapper.getHighLikeExerciseSet(subjectId);
        List<ExerciseSetDTO> exerciseSetDTOS = new ArrayList<>();
        for (ExerciseSet exerciseSet : exerciseSets) {
            Subject subject = subjectMapper.getSubjectById(exerciseSet.getSubjectId());
            List<Exercise> list = midMapper.getExerciseListByExerciseSetId(exerciseSet.getExerciseSetId());
            ExerciseSetDTO exerciseSetDTO = new ExerciseSetDTO(exerciseSet.getExerciseSetId(),exerciseSet.getTitle(),exerciseSet.getCreateTime(),subject,null,null,list, null,exerciseSet.getLikeCount());
            exerciseSetDTOS.add(exerciseSetDTO);
        }
        return exerciseSetDTOS;
    }
}
