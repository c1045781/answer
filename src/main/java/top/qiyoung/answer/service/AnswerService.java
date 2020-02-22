package top.qiyoung.answer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import top.qiyoung.answer.DTO.HistoryAnswerDTO;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.mapper.AnswerMapper;
import top.qiyoung.answer.mapper.ExerciseMapper;
import top.qiyoung.answer.mapper.SubjectMapper;
import top.qiyoung.answer.model.Answer;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.model.Subject;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AnswerService {
    @Resource
    private AnswerMapper answerMapper;
    @Resource
    private ExerciseMapper exerciseMapper;
    @Resource
    private SubjectMapper subjectMapper;

    public void addOrUpdate(Answer answer) {
        Answer dbAnswer = answerMapper.findAnswerByExerciseIdAndUserId(answer.getExerciseId(), answer.getUserId());
        if (dbAnswer == null){
            answerMapper.add(answer);
        }else{
            answer.setCreateTime(new Date());
            answerMapper.update(answer);
        }
    }

    public PaginationDTO<HistoryAnswerDTO> findHistoryAnswer(Integer userId, Integer currentPage, Integer pageSize) {
        List<Answer> list = answerMapper.findAnswerByUserId(userId,(currentPage-1)*pageSize,pageSize);
        int count = answerMapper.countAnswerByUserId(userId);
        List<HistoryAnswerDTO> historyAnswerDTOList = new ArrayList<>();
        for (Answer answer : list) {
            Exercise exercise = exerciseMapper.getExerciseById(answer.getExerciseId());
            Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
            HistoryAnswerDTO dto = new HistoryAnswerDTO(answer.getAnswerId(),answer.getUserId(),answer.getAnswer(),exercise,subject,answer.getCreateTime());
            historyAnswerDTOList.add(dto);
        }
        int totalPage = (int) Math.ceil((double) count / (double) pageSize);
        PaginationDTO<HistoryAnswerDTO> paginationDTO = new PaginationDTO<>(currentPage,pageSize,totalPage,count,null,null,historyAnswerDTOList);
        return paginationDTO;
    }


    public Answer findAnswerByExerciseIdAndUserId(Integer exerciseId, Integer userId) {
        return answerMapper.findAnswerByExerciseIdAndUserId(exerciseId,userId);
    }
}
