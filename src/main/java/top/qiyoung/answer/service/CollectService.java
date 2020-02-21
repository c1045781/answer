package top.qiyoung.answer.service;

import org.springframework.stereotype.Service;
import top.qiyoung.answer.mapper.CollectMapper;
import top.qiyoung.answer.model.Collect;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CollectService {

    @Resource
    private CollectMapper collectMapper;

    public void addCollect(Integer exerciseId,Integer userId) {
        collectMapper.addCollect(exerciseId,userId);

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
}
