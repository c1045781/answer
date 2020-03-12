package top.qiyoung.answer.controller.manager;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.qiyoung.answer.DTO.ExerciseDTO;
import top.qiyoung.answer.DTO.ExerciseEditDTO;
import top.qiyoung.answer.DTO.ExerciseReviewDTO;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.Option;
import top.qiyoung.answer.model.Subject;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.ExerciseService;
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
@RequestMapping("/manager/exercise")
public class ManagerExerciseController {

    @Resource
    private ExerciseService exerciseService;

    @Resource
    private SubjectService subjectService;

    // 跳转习题添加页面
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "manage/exercise/add-exercise";
    }

    // 查询习题
    @RequestMapping("/check")
    public String checkexercise(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                @RequestParam(value = "search", required = false) String search,
                                @RequestParam(value = "type", defaultValue = "title") String type,
                                @RequestParam(value = "exerciseType", required = false) String exerciseType,
                                @RequestParam(value = "subjectId", required = false) Integer subjectId,
                                @RequestParam(value = "order", defaultValue = "exercise_id asc") String orderby,
                                @RequestParam(value = "score", required = false) Integer score,
                                HttpServletRequest request,
                                Model model) {
        PaginationDTO<ExerciseDTO> paginationDTO = exerciseService.getExerciseList(currentPage, size, search, type, orderby, subjectId,exerciseType,score);
        if (subjectId != null) {
            Subject subject = subjectService.getSubjectById(subjectId);
            List<Subject> subjects = subjectService.getSubjectByBase(subject.getBaseSubject());
            List<String> baseList = subjectService.getBase();
            model.addAttribute("subjectList",subjects);
            model.addAttribute("baseList",baseList);
        }
        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("exerciseType", exerciseType);
        model.addAttribute("search", search);
        model.addAttribute("type", type);
        model.addAttribute("score", score);

        User user = (User) request.getSession().getAttribute("user");
        return "manage/exercise/exercise";
    }

    // 获取审核的习题
    @RequestMapping("review")
    public String review(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                         @RequestParam(value = "size", defaultValue = "10") Integer size,
                         @RequestParam(value = "subjectId", required = false) Integer subjectId,
                         @RequestParam(value = "order", defaultValue = "create_time asc") String order,
                         Model model){
        PaginationDTO<ExerciseReviewDTO> paginationDTO = exerciseService.review(currentPage,size,subjectId,order);
        if (subjectId != null) {
            Subject subject = subjectService.getSubjectById(subjectId);
            List<Subject> subjects = subjectService.getSubjectByBase(subject.getBaseSubject());
            List<String> baseList = subjectService.getBase();
            model.addAttribute("subjectList",subjects);
            model.addAttribute("baseList",baseList);
        }
        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("subjectId",subjectId);
        return "manage/review/exercise-review";
    }

    // 跳转习题上传页面
    @RequestMapping("/toUpload")
    public String toUpload() {
        return "manage/exercise/upload";
    }
    // 跳转习题上传页面
    @RequestMapping("/toTemplateUpload")
    public String toTemplateUpload() {
        return "manage/exercise/template-upload";
    }

    //模板文件上传
    @RequestMapping("/templatesUpload")
    public String templatesUpload(@RequestParam("exerciseFile") MultipartFile exerciseFile) throws IOException {
        FileUpload fileUpload = new FileUpload();
        fileUpload.uploadTempla(exerciseFile);
        return "redirect:/index";
    }

    // 习题文件上传
    @RequestMapping("/uploadFile")
    public String upload(@RequestParam("exerciseFile") MultipartFile exerciseFile, HttpServletRequest request,
                         Model model) throws IOException {
//        User user = (User) request.getSession().getAttribute("user");
        FileUpload fileUpload = new FileUpload();
        String upload = fileUpload.upload(exerciseFile);
        upload = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\" + upload;
        //1.读取Excel文档对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(upload));
        //2.获取要解析的表格（第一个表格）
        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
        //获得最后一行的行号
        int lastRowNum = sheet.getLastRowNum();
        List<ExerciseEditDTO> exerciseEditDTOList = new ArrayList<>();
        for (int i = 1; i < lastRowNum; i++) {
            ExerciseEditDTO edit = new ExerciseEditDTO();
            HSSFRow row = sheet.getRow(i);
            String baseSubject = row.getCell(0).getStringCellValue();
            String subjectName = row.getCell(1).getStringCellValue();
            String exercisetype = row.getCell(2).getStringCellValue();
            String title = row.getCell(3).getStringCellValue();
            String answers = row.getCell(4).getStringCellValue();
            String correct = row.getCell(5).getStringCellValue();
            String analysis = row.getCell(6).getStringCellValue();

            Subject subject = subjectService.verification(baseSubject, subjectName);
            if (subject != null) {
                edit.setSubjectId(subject.getSubjectId());
            } else {
                model.addAttribute("messs", "文件格式错误");
                return "manage/exercise/upload";
            }
            if (exercisetype.equals("单选题") || exercisetype.equals("判断题") || exercisetype.equals("多选题")) {
                edit.setExerciseType(exercisetype);
            } else {
                model.addAttribute("messs", "文件格式错误");
                return "manage/exercise/upload";
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
            edit.setAnalysis(analysis);
            exerciseEditDTOList.add(edit);
        }

        DeleteFile deleteFile = new DeleteFile();
        String s = deleteFile.delFile(upload);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (ExerciseEditDTO edit : exerciseEditDTOList) {
            exerciseService.insert(edit, userDetails);
        }
        return "redirect:/manager/exercise/check";
    }

    // 添加或更新习题
    @RequestMapping("/addOrUpdate")
    public String addexercise(ExerciseEditDTO edit, HttpServletRequest request, Model model, HttpServletResponse response) {
//        User user = (User) request.getSession().getAttribute("user");
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
        int result;
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (edit.getExerciseEditId() != null) {
            result = exerciseService.update(edit,userDetails);
        } else {
            result = exerciseService.insert(edit, userDetails);
        }
        if (result <= 0){
            ExerciseEditDTO exerciseEditDTO = edit;
            if (edit.getExerciseEditId() != null)
                exerciseEditDTO = exerciseService.getExerciseEdit(edit.getExerciseEditId());
            model.addAttribute("exerciseEditDTO", exerciseEditDTO);
            return "manage/exercise/add-exercise";
        }else{
            return "redirect:/manager/exercise/check";
        }
    }

    // 跳转到习题更新
    @RequestMapping("/toUpdate")
    public String toUpdate(Integer exerciseId, Model model) {
        ExerciseEditDTO exerciseEditDTO = exerciseService.getExerciseEdit(exerciseId);
        model.addAttribute("exerciseEditDTO", exerciseEditDTO);
        return "manage/exercise/add-exercise";
    }

    // 删除习题
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(Integer exerciseId) {
        exerciseService.deleteByExerciseId(exerciseId);
        return "success";
    }

    // 根据基础学科获取习题
    @RequestMapping("/getExerciseBySubjectId")
    @ResponseBody
    public PaginationDTO<ExerciseDTO> getexerciseBySubjectId(Integer subjectId,
                                                             @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                             @RequestParam(value = "search", required = false) String search,
                                                             @RequestParam(value = "type", required = false) String type,
                                                             @RequestParam(value = "order", defaultValue = "exercise_id asc") String order) {
        if (subjectId == null) {
            return null;
        }
        return exerciseService.getExerciseBySubjectId(subjectId, currentPage, size, search, type, order);
    }

    // 根据习题ID获取习题内容
    @RequestMapping("/viewExercise")
    @ResponseBody
    public ExerciseDTO viewExercise(Integer exerciseId){
        ExerciseDTO  exerciseDTO = exerciseService.getExerciseDTOByExerciseId(exerciseId);
        return exerciseDTO;
    }

    // 修改习题状态
    @RequestMapping("/status")
    @ResponseBody
    public String updateStatus(Integer exerciseId,Integer status,String reason,Integer messageId){
        exerciseService.updateSatus(exerciseId,status,reason,messageId);
        return "success";
    }

}
