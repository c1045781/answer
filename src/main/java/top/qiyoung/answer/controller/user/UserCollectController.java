package top.qiyoung.answer.controller.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.dto.CollectDTO;
import top.qiyoung.answer.dto.PaginationDTO;
import top.qiyoung.answer.dto.ResultDTO;
import top.qiyoung.answer.exception.CustomizeErrorCode;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.service.CollectService;
import top.qiyoung.answer.service.ExerciseService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user/collect")
public class UserCollectController {

    @Resource
    private CollectService collectService;
    @Resource
    private ExerciseService exerciseService;

    // 添加习题收藏
    @RequestMapping("/addCollect")
    @ResponseBody
    public ResultDTO addCollect(Integer exerciseId){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (exerciseId != null){
            Exercise exercise = exerciseService.getExerciseByExerciseId(exerciseId);
            if (exercise!=null){
                collectService.addCollect(exerciseId, userDetails);
            }else {
                return ResultDTO.errorOf(CustomizeErrorCode.EXERCISE_NOT_FOUND);
            }
        }else {
            return ResultDTO.errorOf(CustomizeErrorCode.EXERCISE_NOT_FOUND);
        }
        return ResultDTO.okOf();
    }

    // 删除习题收藏
    @RequestMapping("/deleteCollect")
    @ResponseBody
    public ResultDTO deleteCollect(Integer exerciseId){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (exerciseId != null){
            Exercise exercise = exerciseService.getExerciseByExerciseId(exerciseId);
            if (exercise != null){
                collectService.deleteCollect(exercise.getExerciseId(), userDetails);
            }else {
                return ResultDTO.errorOf(CustomizeErrorCode.EXERCISE_NOT_FOUND);
            }
        }else {
            return ResultDTO.errorOf(CustomizeErrorCode.EXERCISE_NOT_FOUND);
        }
        return ResultDTO.okOf();
    }

    // 获取收藏列表
    @RequestMapping("/findCollectList")
    @ResponseBody
    public PaginationDTO<CollectDTO> findCollectList(Integer currentPage,
                                                 @RequestParam(defaultValue = "10") Integer pageSize){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PaginationDTO<CollectDTO> paginationDTO = collectService.findCollectList(userDetails,currentPage,pageSize);
        return paginationDTO;
    }

    // 查找用户收藏
    @RequestMapping("/collection")
    @ResponseBody
    public List<Integer> collection(@RequestBody List<String> exerciseId){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Integer> dbExerciseIdList = collectService.collection(exerciseId, userDetails);
        return dbExerciseIdList;
    }
}
