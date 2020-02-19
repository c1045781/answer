package top.qiyoung.answer.service;

import org.springframework.stereotype.Service;
import top.qiyoung.answer.mapper.AnswerMapper;
import top.qiyoung.answer.model.Answer;

import javax.annotation.Resource;

@Service
public class AnswerService {
    @Resource
    private AnswerMapper answerMapper;

    public void add(Answer answer) {
        answerMapper.add(answer);
    }
}
