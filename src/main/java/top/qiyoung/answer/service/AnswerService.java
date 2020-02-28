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
        answerMapper.add(answer);
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


    public Answer findAnswerByAnswerId(Integer answerId) {
        return answerMapper.findAnswerByAnswerId(answerId);
    }

    public PaginationDTO<HistoryAnswerDTO> findWrongAnswer(Integer userId, Integer currentPage, Integer pageSize, String search, Integer subjectId) {
        List<Integer> idList = answerMapper.findExerciseIdByUserId(userId,subjectId,search);
        List<Answer> list = new ArrayList<>();
        for (Integer id : idList) {
            Answer answer = answerMapper.findWrongAnswerByUserIdAndExerciseId(id, userId);
            if (answer != null) list.add(answer);
        }
        int count = list.size();
        List<HistoryAnswerDTO> historyAnswerDTOList = new ArrayList<>();
        int end = currentPage*pageSize > list.size()?list.size():currentPage*pageSize;
        for (int i = (currentPage-1)*pageSize; i < end;i++) {
            Exercise exercise = exerciseMapper.getExerciseById(list.get(i).getExerciseId());
            Subject subject = subjectMapper.getSubjectById(exercise.getSubjectId());
            HistoryAnswerDTO dto = new HistoryAnswerDTO(list.get(i).getAnswerId(),list.get(i).getUserId(),list.get(i).getAnswer(),exercise,subject,list.get(i).getCreateTime());
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
