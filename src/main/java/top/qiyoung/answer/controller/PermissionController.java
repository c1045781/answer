package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.Message;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.PermissionService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    // 查询申请
    @RequestMapping("/check")
    public String check(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                        @RequestParam(value = "size", defaultValue = "2") Integer size,
                        @RequestParam(value = "order", defaultValue = "create_time asc") String order,
                        Model model){
        PaginationDTO<Message> paginationDTO = permissionService.getMessageList(currentPage,size,order);
        model.addAttribute("paginationDTO",paginationDTO);
        return "manage/review/permission-review";
    }

    // 更新用户类型
    @RequestMapping("/role")
    @ResponseBody
    public String updateStatus(Integer id,Integer role,Integer userId){
        permissionService.updateRole(id,role,userId);
        return "success";
    }

    // 获取单个申请
    @RequestMapping("/checkOfOne")
    @ResponseBody
    public Message checkOfOne(Integer id){
        return permissionService.getMessageById(id);
    }

    @RequestMapping("/permissionList")
    @ResponseBody
    public List<Message> permissionList(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        return permissionService.getMessageListByUserId(user.getUserId());
    }

    @RequestMapping("/add")
    @ResponseBody
    public String add(HttpServletRequest request,String content){
        User user = (User) request.getSession().getAttribute("user");
        Message message = new Message(null,content,0,new Date(),user.getUserId(),null);
        permissionService.add(message);
        return "success";
    }
}
