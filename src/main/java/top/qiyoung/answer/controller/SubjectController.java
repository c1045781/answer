package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.Subject;
import top.qiyoung.answer.service.SubjectService;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("subject")
public class SubjectController {

    @Resource
    private SubjectService subjectService;

    // 跳转添加学科页面
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "manage/subject/add-subject";
    }

    // 查询学科
    @RequestMapping("/check")
    public String chech(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                        @RequestParam(value = "size", defaultValue = "2") Integer size,
                        @RequestParam(value = "type", required = false) String type,
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "order", defaultValue = "subject_id asc") String order,
                        Model model) {
        PaginationDTO<Subject> paginationDTO = subjectService.getSubjectList(currentPage, size, type, search, order);
        List<String> baseList = subjectService.getBase();
        model.addAttribute("baseList", baseList);
        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("type", type);
        model.addAttribute("search", search);
        return "manage/subject/subject";
    }

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

    // 删除学科
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(Integer subjectId) {
        int i = subjectService.delete(subjectId);
        if (i <= 0) {
            return "failure";
        }
        return "success";
    }

    // 跳转学科更新页面
    @RequestMapping("/toUpdate")
    public String toUpdate(Integer subjectId, Model model) {
        Subject sub = subjectService.getSubjectById(subjectId);
        List<String> base = subjectService.getBase();
        model.addAttribute("subject", sub);
        model.addAttribute("baseList", base);
        return "manage/subject/add-subject";
    }

    // 添加或更新学科
    @RequestMapping("addOrUpdate")
    public String addOrUpdate(Subject subject, Model model) {
        int result;
        if (StringUtils.isEmpty(subject.getName()) || StringUtils.isEmpty(subject.getBaseSubject())){
            List<String> base = subjectService.getBase();
            model.addAttribute("message", "编辑失败，请填写完整");
            model.addAttribute("subject", subject);
            model.addAttribute("baseList", base);
            return "manage/subject/add-subject";
        }
        if (subject.getSubjectId() != null) {
            result = subjectService.update(subject);
        } else {
            result = subjectService.insert(subject);
        }
        if (result <= 0) {
            List<String> base = subjectService.getBase();
            model.addAttribute("message", "添加失败，题目分类已存在");
            if (subject.getSubjectId() != null)
                model.addAttribute("message", "修改失败,请重新提交");
            model.addAttribute("subject", subject);
            model.addAttribute("baseList", base);
            return "manage/subject/add-subject";
        }
        return "redirect:/subject/check";
    }
}
