package top.qiyoung.answer.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.mapper.EvaluationMapper;
import top.qiyoung.answer.mapper.ExerciseMapper;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.Evaluation;
import top.qiyoung.answer.model.MyUser;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EvaluationService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private EvaluationMapper evaluationMapper;
    @Resource
    private ExerciseMapper exerciseMapper;

    public void addOrUpdate(Integer score,Integer exerciseId, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        Evaluation evaluation = new Evaluation(null, myUser.getUserId(),exerciseId,score);
        Evaluation dbEvaluation = evaluationMapper.getEvaluationByUserIdAndExerciseId(myUser.getUserId(),exerciseId);
        if (dbEvaluation != null){
            evaluation.setEvaluationId(dbEvaluation.getEvaluationId());
            evaluationMapper.update(evaluation);
        }else {
            evaluationMapper.insert(evaluation);
        }
        exerciseMapper.updateScore(findAVGScoreByExerciseId(exerciseId),exerciseId);
    }


    public List<Evaluation> scoreByUserIdAndExerciseId(List<String> exerciseIdList, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        List<Evaluation> evaluations = new ArrayList<>();
        for (String exerciseId : exerciseIdList) {
            Evaluation evaluation = evaluationMapper.getEvaluationByUserIdAndExerciseId(myUser.getUserId(), Integer.parseInt(exerciseId));
            evaluations.add(evaluation);
        }
        return evaluations;
    }

    public Integer countByExerciseId(Integer exerciseId) {
        Integer count = evaluationMapper.countByExerciseId(exerciseId);
        return count;
    }

    public Double findAVGScoreByExerciseId(Integer exerciseId) {
        Double score = evaluationMapper.findAVGScoreByExerciseId(exerciseId);
        return score;
    }
}
