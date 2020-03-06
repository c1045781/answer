package top.qiyoung.answer.controller.manager;

import org.apache.commons.lang3.StringUtils;
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

@Controller
@RequestMapping("/manager/permission")
public class ManagerPermissionController {

    @Resource
    private PermissionService permissionService;

    // 查询申请
    @RequestMapping("/check")
    public String check(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                        @RequestParam(value = "order", defaultValue = "create_time asc") String order,
                        Model model){
        PaginationDTO<Message> paginationDTO = permissionService.getMessageList(currentPage,size,order);
        model.addAttribute("paginationDTO",paginationDTO);
        return "manage/review/permission-review";
    }

    // 更新用户类型
    @RequestMapping("/role")
    @ResponseBody
    public String updateStatus(Integer messageId,Integer role,Integer userId,String reason,Integer status){
        permissionService.updateRole(messageId,role,userId,reason,status);
        return "success";
    }

    // 获取单个申请
    @RequestMapping("/checkOfOne")
    @ResponseBody
    public Message checkOfOne(Integer messageId){
        return permissionService.getMessageByMessageId(messageId);
    }

}
