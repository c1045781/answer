package top.qiyoung.answer.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.DTO.CollectDTO;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.mapper.CollectMapper;
import top.qiyoung.answer.mapper.ExerciseMapper;
import top.qiyoung.answer.mapper.SubjectMapper;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.Collect;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.model.Subject;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CollectService {

    @Resource
    private CollectMapper collectMapper;
    @Resource
    private ExerciseMapper exerciseMapper;
    @Resource
    private SubjectMapper subjectMapper;
    @Resource
    private UserMapper userMapper;

    public void addCollect(Integer exerciseId, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        collectMapper.addCollect(new Collect(null, myUser.getUserId(),exerciseId,new Date()));

    }

    public void deleteCollect(Integer exerciseId, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        collectMapper.deleteCollect(exerciseId, myUser.getUserId());
    }

    public List<Integer> collection(List<String> exerciseId, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        List<Integer> list = new ArrayList<>();
        for (String id : exerciseId) {
            Collect collect = collectMapper.findCollectByExerciseIdAndUserId(Integer.parseInt(id), myUser.getUserId());
            if (collect != null) {
                list.add(collect.getExerciseId());
            }
        }
        return list;
    }

    public PaginationDTO<CollectDTO> findCollectList(UserDetails userDetails, Integer currentPage, Integer pageSize) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        List<Collect> list = collectMapper.findCollectByUserId(myUser.getUserId(),(currentPage-1)*pageSize,pageSize);
        int count = collectMapper.countCollectByUserId(myUser.getUserId());
        List<CollectDTO> collectDTOList = new ArrayList<>();
        for (Collect collect : list) {
            Exercise exercise = exerciseMapper.getExerciseByExerciseId(collect.getExerciseId());
            Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
            CollectDTO dto = new CollectDTO(collect,exercise,subject);
            collectDTOList.add(dto);
        }
        int totalPage = (int) Math.ceil((double) count / (double) pageSize);
        PaginationDTO<CollectDTO> paginationDTO = new PaginationDTO<>(currentPage,pageSize,totalPage,count,null,null,collectDTOList);
        return paginationDTO;
    }
}
