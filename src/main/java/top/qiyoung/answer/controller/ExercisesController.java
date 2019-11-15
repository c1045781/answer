package top.qiyoung.answer.controller;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.qiyoung.answer.mapper.SubjectMapper;
import top.qiyoung.answer.model.*;
import top.qiyoung.answer.service.ExercisesService;
import top.qiyoung.answer.service.SubjectService;
import top.qiyoung.answer.utils.DeleteFile;
import top.qiyoung.answer.utils.FileUpload;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/exercises")
public class ExercisesController {

    @Resource
    private ExercisesService exercisesService;

    @Resource
    private SubjectService subjectService;

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "exercises/add-exercises";
    }

    @RequestMapping("/check")
    public String checkExercises(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                 @RequestParam(value = "size", defaultValue = "10") Integer size,
                                 @RequestParam(value = "search", required = false) String search,
                                 @RequestParam(value = "exercisesType", required = false) String type,
                                 @RequestParam(value = "order", defaultValue = "id") String order,
                                 Model model) {
        Pagination pagination = exercisesService.getExercisesVMList(currentPage, size, search, type, order);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("type", type);
        return "exercises/exercises";
    }

    @RequestMapping("/toUpload")
    public String toUpload() {
        return "exercises/upload";
    }

    @RequestMapping("/uploadFile")
    public String upload(@RequestParam("exercisesFile") MultipartFile exercisesFile, HttpServletRequest request,
                         Model model) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        FileUpload fileUpload = new FileUpload();
        String upload = fileUpload.upload(exercisesFile);
        upload = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\" + upload;
        //1.读取Excel文档对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(upload));
        //2.获取要解析的表格（第一个表格）
        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
        //获得最后一行的行号
        int lastRowNum = sheet.getLastRowNum();
        List<ExercisesEdit> exercisesEditList = new ArrayList<>();
        for (int i = 1; i <= lastRowNum; i++) {
            ExercisesEdit edit = new ExercisesEdit();
            HSSFRow row = sheet.getRow(i);
            String baseSubject = row.getCell(0).getStringCellValue();
            String subjectName = row.getCell(1).getStringCellValue();
            String exercisestype = row.getCell(2).getStringCellValue();
            String title = row.getCell(3).getStringCellValue();
            String answers = row.getCell(4).getStringCellValue();
            String correct = row.getCell(5).getStringCellValue();

            Subject subject = subjectService.verification(baseSubject, subjectName);
            if (subject != null) {
                edit.setSubjectId(subject.getId());
            } else {
                model.addAttribute("messs", "文件格式错误");
                return "exercises/upload";
            }
            if (exercisestype.equals("单选题") || exercisestype.equals("判断题") || exercisestype.equals("多选题")) {
                edit.setExercisesType(exercisestype);
            } else {
                model.addAttribute("messs", "文件格式错误");
                return "exercises/upload";
            }
            edit.setTitle(title);
            edit.setCorrect(correct);
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
            exercisesEditList.add(edit);
        }

        DeleteFile deleteFile = new DeleteFile();
        deleteFile.delFile(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\" + upload);

        for (ExercisesEdit edit : exercisesEditList) {
            exercisesService.insert(edit, user);
        }
        return "redirect:/exercises/check";
    }

    @RequestMapping("/addOrUpdate")
    public String addExercises(ExercisesEdit edit, HttpServletRequest request) {
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
        if (edit.getId() != null) {
            exercisesService.update(edit);
        } else {
            exercisesService.insert(edit, user);
        }
        return "redirect:/exercises/check";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id, Model model) {
        ExercisesEdit exercisesEdit = exercisesService.getExercisesEdit(id);
        model.addAttribute("exercisesEdit", exercisesEdit);
        return "exercises/add-exercises";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public void delete(@RequestBody Exercises exercises) {
        exercisesService.deleteById(exercises.getId());
    }
}
