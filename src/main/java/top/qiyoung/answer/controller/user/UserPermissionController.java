package top.qiyoung.answer.controller.user;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.dto.PaginationDTO;
import top.qiyoung.answer.dto.ResultDTO;
import top.qiyoung.answer.model.Message;
import top.qiyoung.answer.service.PermissionService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user/permission")
public class UserPermissionController {

    @Resource
    private PermissionService permissionService;


    // 获取单个申请
    @RequestMapping("/checkOfOne")
    @ResponseBody
    public Message checkOfOne(Integer messageId){
        return permissionService.getMessageByMessageId(messageId);
    }

    @RequestMapping("/permissionList")
    @ResponseBody
    public PaginationDTO<Message> permissionList(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size){
//        MyUser myUser = (MyUser) request.getSession().getAttribute("myUser");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return permissionService.getMessageListByUserId(userDetails,currentPage,size);
    }

    @RequestMapping("/add")
    @ResponseBody
    public ResultDTO add(String content){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isNotBlank(content)){
            permissionService.add(content, userDetails);
        }
        return ResultDTO.okOf();
    }
}
