package top.qiyoung.answer.controller.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.DTO.CollectDTO;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.service.CollectService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user/collect")
public class UserCollectController {

    @Resource
    private CollectService collectService;


    // 添加习题收藏
    @RequestMapping("/addCollect")
    @ResponseBody
    public String addCollect(Integer exerciseId,HttpServletRequest request){
//        MyUser myUser = (MyUser) request.getSession().getAttribute("myUser");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        collectService.addCollect(exerciseId, userDetails);
        return "success";
    }

    // 删除习题收藏
    @RequestMapping("/deleteCollect")
    @ResponseBody
    public String deleteCollect(Integer exerciseId,HttpServletRequest request){
//        MyUser myUser = (MyUser) request.getSession().getAttribute("myUser");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        collectService.deleteCollect(exerciseId, userDetails);
        return "success";
    }

    // 获取收藏列表
    @RequestMapping("/findCollectList")
    @ResponseBody
    public PaginationDTO<CollectDTO> findCollectList(HttpServletRequest request,
                                                 Integer currentPage,
                                                 @RequestParam(defaultValue = "10") Integer pageSize){
//        MyUser myUser = (MyUser) request.getSession().getAttribute("myUser");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PaginationDTO<CollectDTO> paginationDTO = collectService.findCollectList(userDetails,currentPage,pageSize);
        return paginationDTO;
    }

    // 查找用户收藏
    @RequestMapping("/collection")
    @ResponseBody
    public List<Integer> collection(@RequestBody List<String> exerciseId, HttpServletRequest request){
//        MyUser myUser = (MyUser) request.getSession().getAttribute("myUser");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Integer> dbExerciseIdList = collectService.collection(exerciseId, userDetails);
        return dbExerciseIdList;
    }
}
