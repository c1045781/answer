package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.model.Subject;
import top.qiyoung.answer.service.SubjectService;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/subject")
public class SubjectController {

    @Resource
    private SubjectService subjectService;


    // 查询基础学科
    @RequestMapping("/subjectByBase")
    @ResponseBody
    public List<Subject> findSubjectByBase(@RequestBody Subject subject) {
        List<Subject> subjectList = subjectService.getSubjectByBase(subject.getBaseSubject());
        return subjectList;
    }

    // 获取所有基础学科
    @RequestMapping("/base")
    @ResponseBody
    public List<String> base() {
        List<String> base = subjectService.getBase();
        return base;
    }
}
