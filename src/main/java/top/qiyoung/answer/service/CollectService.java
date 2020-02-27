package top.qiyoung.answer.service;

import org.springframework.stereotype.Service;
import top.qiyoung.answer.DTO.CollectDTO;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.mapper.CollectMapper;
import top.qiyoung.answer.mapper.ExerciseMapper;
import top.qiyoung.answer.mapper.SubjectMapper;
import top.qiyoung.answer.model.Collect;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.Subject;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CollectService {

    @Resource
    private CollectMapper collectMapper;
    @Resource
    private ExerciseMapper exerciseMapper;
    @Resource
    private SubjectMapper subjectMapper;

    public void addCollect(Collect collect) {
        collectMapper.addCollect(collect);

    }

    public void deleteCollect(Integer exerciseId,Integer userId) {
        collectMapper.deleteCollect(exerciseId,userId);
    }

    public List<Integer> collection(List<String> exerciseId,Integer userId) {
        List<Integer> list = new ArrayList<>();
        for (String id : exerciseId) {
            Collect collect = collectMapper.findCollectByExerciseIdAndUserId(Integer.parseInt(id),userId);
            if (collect != null) {
                list.add(collect.getExerciseId());
            }
        }
        return list;
    }

    public PaginationDTO<CollectDTO> findCollect(Integer userId, Integer currentPage, Integer pageSize) {
        List<Collect> list = collectMapper.findCollectByUserId(userId,(currentPage-1)*pageSize,pageSize);
        int count = collectMapper.countCollectByUserId(userId);
        List<CollectDTO> collectDTOList = new ArrayList<>();
        for (Collect collect : list) {
            Exercise exercise = exerciseMapper.getExerciseById(collect.getExerciseId());
            Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
            CollectDTO dto = new CollectDTO(collect,exercise,subject);
            collectDTOList.add(dto);
        }
        int totalPage = (int) Math.ceil((double) count / (double) pageSize);
        PaginationDTO<CollectDTO> paginationDTO = new PaginationDTO<>(currentPage,pageSize,totalPage,count,null,null,collectDTOList);
        return paginationDTO;
    }
}
