package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.DTO.CollectDTO;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.Collect;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.CollectService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/collect")
public class CollectController {

    @Resource
    private CollectService collectService;

    // 查找用户收藏
    @RequestMapping("/collection")
    @ResponseBody
    public List<Integer> collection(@RequestBody List<String> exerciseId, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        List<Integer> dbExerciseIdList = collectService.collection(exerciseId,user.getUserId());
        return dbExerciseIdList;
    }

    // 添加习题收藏
    @RequestMapping("/addCollect")
    @ResponseBody
    public String addCollect(Integer exerciseId,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        Collect collect = new Collect(null,user.getUserId(),exerciseId,new Date());
        collectService.addCollect(collect);
        return "success";
    }

    // 删除习题收藏
    @RequestMapping("/deleteCollect")
    @ResponseBody
    public String deleteCollect(Integer exerciseId,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        collectService.deleteCollect(exerciseId,user.getUserId());
        return "success";
    }

    // 获取收藏列表
    @RequestMapping("/findCollectList")
    @ResponseBody
    public PaginationDTO<CollectDTO> findCollectList(HttpServletRequest request,
                                                 Integer currentPage,
                                                 @RequestParam(defaultValue = "3") Integer pageSize){
        User user = (User) request.getSession().getAttribute("user");
        PaginationDTO<CollectDTO> paginationDTO = collectService.findCollectList(user.getUserId(),currentPage,pageSize);
        return paginationDTO;
    }
}
