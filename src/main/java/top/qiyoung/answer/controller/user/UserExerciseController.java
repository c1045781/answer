package top.qiyoung.answer.controller.user;

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
import top.qiyoung.answer.model.*;
import top.qiyoung.answer.service.EvaluationService;
import top.qiyoung.answer.service.ExerciseService;
import top.qiyoung.answer.service.SubjectService;
import top.qiyoung.answer.utils.DeleteFile;
import top.qiyoung.answer.utils.FileUpload;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/user/exercise")
public class UserExerciseController {

    @Resource
    private ExerciseService exerciseService;

    @Resource
    private SubjectService subjectService;

    @Resource
    private EvaluationService evaluationService;


    // 查询习题
    @RequestMapping("/check")
    public String checkexercise(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                @RequestParam(value = "search", required = false) String search,
                                @RequestParam(value = "type", defaultValue = "title") String type,
                                @RequestParam(value = "exerciseType", required = false) String exerciseType,
                                @RequestParam(value = "subjectId", required = false) Integer subjectId,
                                @RequestParam(value = "order", defaultValue = "exercise_id asc") String orderby,
                                HttpServletRequest request,
                                Model model) {
        PaginationDTO<ExerciseDTO> paginationDTO = exerciseService.getExerciseList(currentPage, size, search, type, orderby, subjectId,exerciseType,null);
        if (subjectId != null) {
            Subject subject = subjectService.getSubjectById(subjectId);
            List<Subject> subjects = subjectService.getSubjectByBase(subject.getBaseSubject());
            List<String> baseList = subjectService.getBase();
            model.addAttribute("subjectList",subjects);
            model.addAttribute("baseList",baseList);
        }
        Map<Integer,Integer> countMap = new HashMap<>();
        Map<Integer,Double> AVGScores = new HashMap<>();
        for (ExerciseDTO exerciseDTO : paginationDTO.getDataList()) {
            Integer count = evaluationService.countByExerciseId(exerciseDTO.getExercise().getExerciseId());
            Double score = evaluationService.findAVGScoreByExerciseId(exerciseDTO.getExercise().getExerciseId());
            AVGScores.put(exerciseDTO.getExercise().getExerciseId(),score);
            countMap.put(exerciseDTO.getExercise().getExerciseId(),count);
        }
        model.addAttribute("AVGScores", AVGScores);
        model.addAttribute("countMap", countMap);
        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("exerciseType", exerciseType);
        model.addAttribute("search", search);
        model.addAttribute("type", type);
        return "user/exercise";
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
            }
            edit.setExerciseType(exercisetype);
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
            exerciseService.insert(edit,userDetails);
        }
        return "redirect:/user/personal";
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
            result = exerciseService.insert(edit,userDetails);
        }
        if (result <= 0){
            ExerciseEditDTO exerciseEditDTO = edit;
            if (edit.getExerciseEditId() != null)
                exerciseEditDTO = exerciseService.getExerciseEdit(edit.getExerciseEditId());
            model.addAttribute("exerciseEditDTO", exerciseEditDTO);
        }
        return "redirect:/user/personal";
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

    // 根据ID获取习题并跳转到答题页面
    @RequestMapping("/exerciseToAnswer")
    public String exerciseToAnswer(Integer exerciseId, Model model) {
        ExerciseEditDTO exerciseEditDTO = exerciseService.getExerciseEdit(exerciseId);
        List<ExerciseEditDTO> exerciseEditDTOList = new ArrayList();
        exerciseEditDTOList.add(exerciseEditDTO);
        model.addAttribute("exerciseEditDTOList", exerciseEditDTOList);
        return "user/answer";
    }


    // 根据习题集ID获取习题并跳转到答题页面
    @RequestMapping("/exerciseSetToAnswer")
    public String exerciseSetToAnswer(Integer exerciseSetId, Model model) {
        List<Integer> exerciseIdList = exerciseService.getExerciseIdListByExerciseSetId(exerciseSetId);
        List<ExerciseEditDTO> exerciseEditDTOList = new ArrayList();
        for (Integer exerciseId : exerciseIdList) {
            ExerciseEditDTO exerciseEditDTO = exerciseService.getExerciseEdit(exerciseId);
            exerciseEditDTOList.add(exerciseEditDTO);
        }
        model.addAttribute("exerciseEditDTOList", exerciseEditDTOList);
        return "user/answer";
    }

    // 通过用户ID获取用户上传题目
    @RequestMapping("/getReviewExerciseByUserId")
    @ResponseBody
    public PaginationDTO<Exercise> getReviewExerciseByUserId(HttpServletRequest request,
                                                             @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                             @RequestParam(value = "status") Integer status){
//        User user = (User) request.getSession().getAttribute("user");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PaginationDTO<Exercise> dto = exerciseService.getReviewExerciseByUserId(userDetails,currentPage,size,status);
        return dto;
    }

    // 获取单个上传题目
    @RequestMapping("/checkOfAddExercise")
    @ResponseBody
    public ExerciseReviewDTO checkOfAddExercise(Integer exerciseId,HttpServletRequest request){
//        User user = (User) request.getSession().getAttribute("user");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ExerciseReviewDTO dto = exerciseService.checkOfAddExercise(exerciseId,userDetails);
        return dto;
    }

    // 跳转到用户习题更新
    @RequestMapping("/toUpdate")
    @ResponseBody
    public ExerciseEditDTO toUpdate(Integer exerciseId) {
        ExerciseEditDTO exerciseEditDTO = exerciseService.getExerciseEdit(exerciseId);
        return exerciseEditDTO;
    }

}
