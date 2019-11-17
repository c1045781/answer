package top.qiyoung.answer.controller;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.qiyoung.answer.model.*;
import top.qiyoung.answer.service.ExerciseService;
import top.qiyoung.answer.service.SubjectService;
import top.qiyoung.answer.utils.DeleteFile;
import top.qiyoung.answer.utils.FileUpload;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/exercise")
public class ExerciseController {

    @Resource
    private ExerciseService exerciseService;

    @Resource
    private SubjectService subjectService;

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "exercise/add-exercise";
    }

    @RequestMapping("/check")
    public String checkexercise(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                @RequestParam(value = "size", defaultValue = "2") Integer size,
                                @RequestParam(value = "search", required = false) String search,
                                @RequestParam(value = "type", required = false) String type,
                                @RequestParam(value = "exerciseType", required = false) String exerciseType,
                                @RequestParam(value = "subjectId", required = false) Integer subjectId,
                                @RequestParam(value = "order", defaultValue = "exercise_id asc") String order,
                                Model model) {
        Pagination<ExerciseVM> pagination = exerciseService.getExerciseList(currentPage, size, search, type, order, subjectId,exerciseType);
        if (subjectId != null) {
            Subject subject = subjectService.getSubjectById(subjectId);
            List<Subject> subjects = subjectService.getSubjectByBase(subject.getBaseSubject());
            List<String> baseList = subjectService.getBase();
            model.addAttribute("subjectList",subjects);
            model.addAttribute("baseList",baseList);
        }
        model.addAttribute("pagination", pagination);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("exerciseType", exerciseType);
        model.addAttribute("search", search);
        model.addAttribute("type", type);
        return "exercise/exercise";
    }

    @RequestMapping("/toUpload")
    public String toUpload() {
        return "exercise/upload";
    }

    @RequestMapping("/uploadFile")
    public String upload(@RequestParam("exerciseFile") MultipartFile exerciseFile, HttpServletRequest request,
                         Model model) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        FileUpload fileUpload = new FileUpload();
        String upload = fileUpload.upload(exerciseFile);
        upload = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\" + upload;
        //1.读取Excel文档对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(upload));
        //2.获取要解析的表格（第一个表格）
        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
        //获得最后一行的行号
        int lastRowNum = sheet.getLastRowNum();
        List<ExerciseEdit> exerciseEditList = new ArrayList<>();
        for (int i = 1; i < lastRowNum; i++) {
            ExerciseEdit edit = new ExerciseEdit();
            HSSFRow row = sheet.getRow(i);
            String baseSubject = row.getCell(0).getStringCellValue();
            String subjectName = row.getCell(1).getStringCellValue();
            String exercisetype = row.getCell(2).getStringCellValue();
            String title = row.getCell(3).getStringCellValue();
            String answers = row.getCell(4).getStringCellValue();
            String correct = row.getCell(5).getStringCellValue();

            Subject subject = subjectService.verification(baseSubject, subjectName);
            if (subject != null) {
                edit.setSubjectId(subject.getSubjectId());
            } else {
                model.addAttribute("messs", "文件格式错误");
                return "exercise/upload";
            }
            if (exercisetype.equals("单选题") || exercisetype.equals("判断题") || exercisetype.equals("多选题")) {
                edit.setExerciseType(exercisetype);
            } else {
                model.addAttribute("messs", "文件格式错误");
                return "exercise/upload";
            }
            edit.setTitle(title);
            edit.setCorrect(correct.toUpperCase());
            String[] split = answers.split("、");
            List<String> answerList = new ArrayList<>(Arrays.asList(split));
            List<Option> options = new ArrayList<>();
            byte[] bytes = {64};
            for (int j = 0; j < answerList.size(); j++) {
                Option option = new Option();
                bytes[0] += 1;
                String s = new String(bytes);
                option.setOption(s);
                option.setContent(answerList.get(j));
                options.add(option);
            }
            edit.setOptions(options);
            exerciseEditList.add(edit);
        }

        DeleteFile deleteFile = new DeleteFile();
        String s = deleteFile.delFile(upload);
        System.out.println(s);

        for (ExerciseEdit edit : exerciseEditList) {
            exerciseService.insert(edit, user);
        }
        return "redirect:/exercise/check";
    }

    @RequestMapping("/addOrUpdate")
    public String addexercise(ExerciseEdit edit, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        List<Option> options = new ArrayList<>();
        List<String> answers = edit.getAnswers();
        byte[] bytes = {64};
        for (int i = 0; i < answers.size(); i++) {
            Option option = new Option();
            bytes[0] += 1;
            String s = new String(bytes);
            option.setOption(s);
            option.setContent(answers.get(i));
            options.add(option);
        }
        edit.setOptions(options);
        if (edit.getExerciseEditId() != null) {
            exerciseService.update(edit);
        } else {
            exerciseService.insert(edit, user);
        }
        return "redirect:/exercise/check";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer exerciseId, Model model) {
        ExerciseEdit exerciseEdit = exerciseService.getExerciseEdit(exerciseId);
        model.addAttribute("exerciseEdit", exerciseEdit);
        return "exercise/add-exercise";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(Integer exerciseId) {
        exerciseService.deleteById(exerciseId);
        return "success";
    }

    @RequestMapping("/getExerciseBySubjectId")
    @ResponseBody
    public Pagination<ExerciseVM> getexerciseBySubjectId(Integer subjectId,
                                                         @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                                         @RequestParam(value = "size", defaultValue = "2") Integer size,
                                                         @RequestParam(value = "search", required = false) String search,
                                                         @RequestParam(value = "type", required = false) String type,
                                                         @RequestParam(value = "order", defaultValue = "exercise_id asc") String order) {
        if (subjectId == null) {
            return null;
        }
        return exerciseService.getExerciseBySubjectId(subjectId, currentPage, size, search, type, order);
    }
}
