package top.qiyoung.answer.controller.manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.dto.PaginationDTO;
import top.qiyoung.answer.dto.ResultDTO;
import top.qiyoung.answer.enums.MessageTypeEnum;
import top.qiyoung.answer.exception.CustomizeErrorCode;
import top.qiyoung.answer.model.Message;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.service.PermissionService;
import top.qiyoung.answer.service.UserService;

import javax.annotation.Resource;
import java.security.Principal;

@Controller
@RequestMapping("/manager/permission")
public class ManagerPermissionController {

    @Resource
    private PermissionService permissionService;
    @Resource
    private UserService userService;

    // 查询权限申请
    @RequestMapping("/checkAuthority")
    public String checkAuthority(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                        @RequestParam(value = "order", defaultValue = "create_time asc") String order,
                        Model model){
        PaginationDTO<Message> paginationDTO = permissionService.getMessageList(currentPage,size,order, MessageTypeEnum.PERMISSION_APPLICATION.getType());
        model.addAttribute("paginationDTO",paginationDTO);
        return "manage/review/permission-review";
    }

    // 查询通知申请
    @RequestMapping("/checkNotice")
    public String checkNotify(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                        @RequestParam(value = "order", defaultValue = "create_time asc") String order,
                        Model model){
        PaginationDTO<Message> paginationDTO = permissionService.getMessageList(currentPage,size,order,MessageTypeEnum.NOTICE.getType());
        model.addAttribute("paginationDTO",paginationDTO);
        return "manage/review/notice-review";
    }

    // 更新用户类型
    @RequestMapping("/role")
    @ResponseBody
    public ResultDTO updateStatus(Integer messageId, Integer role, Integer userId, String reason, Integer status){
        if (messageId != null && userId != null){
            Message message = permissionService.getMessageByMessageId(messageId);
            MyUser user = userService.getUserByUserId(userId);
            if (message != null && user != null) {
                permissionService.updateRole(message.getMessageId(), role, user.getUserId(), reason, status);
            }else {
                if (message == null){
                    return ResultDTO.errorOf(CustomizeErrorCode.MESSAGE_NOT_FOUND);
                }else {
                    return ResultDTO.errorOf(CustomizeErrorCode.USER_NOT_FOUND);
                }
            }
        }else {
            if (messageId == null){
                return ResultDTO.errorOf(CustomizeErrorCode.MESSAGE_NOT_FOUND);
            }else {
                return ResultDTO.errorOf(CustomizeErrorCode.USER_NOT_FOUND);
            }
        }
        return ResultDTO.okOf();
    }

    // 获取单个申请
    @RequestMapping("/checkOfOne")
    @ResponseBody
    public Message checkOfOne(Integer messageId){
        return permissionService.getMessageByMessageId(messageId);
    }

    @RequestMapping("/addNotice")
    public String addNotification(String content, Integer role, Principal principal,Model model){
        ResultDTO resultDTO = permissionService.addNotice(content, role, principal.getName());
        if (resultDTO.getCode() == 200) {
            model.addAttribute("msg", "添加成功");
            return "redirect:/manager/notification/toAdd";
        }else {
            model.addAttribute("message", resultDTO.getMessage());
            model.addAttribute("content", content);
            return "/manage/notification/add-notice";
        }
    }


}
