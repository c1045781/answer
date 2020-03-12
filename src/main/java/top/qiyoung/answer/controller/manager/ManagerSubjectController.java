package top.qiyoung.answer.controller.manager;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.Subject;
import top.qiyoung.answer.service.SubjectService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/manager/subject")
public class ManagerSubjectController {

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
                        @RequestParam(value = "size", defaultValue = "10") Integer size,
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

    // 删除学科
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(Integer subjectId) {
        subjectService.delete(subjectId);
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
        return "redirect:/manager/subject/check";
    }

    @RequestMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        List<String> base = subjectService.getBase();
        List<List<Subject>> all = new ArrayList();
        for (String b : base) {
            List<Subject> subjects = subjectService.getSubjectByBase(b);
            all.add(subjects);
        }
        //创建工作文档对象
        Workbook wb = new HSSFWorkbook();
        Sheet sheet1 = wb.createSheet("sheet1");
        for (int i = 0; i < all.size(); i++) {
            Row row = sheet1.createRow(i);
            for (int j = 0; j <= all.get(i).size(); j++) {
                Cell cell = row.createCell(j);
                if (j==0){
                    cell.setCellValue(base.get(i));
                }else {
                    cell.setCellValue(all.get(i).get(j-1).getName());
                }
            }
        }
        response.reset();
        OutputStream output=response.getOutputStream();
        response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode("题目分类.xls", "UTF-8"));
        response.setContentType("application/msexcel");
        wb.write(output);
        output.close();
    }
}
