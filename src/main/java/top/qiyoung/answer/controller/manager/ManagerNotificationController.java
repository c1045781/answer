package top.qiyoung.answer.controller.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.dto.ResultDTO;
import top.qiyoung.answer.service.NotificationService;

@Controller
@RequestMapping("/manager/notification")
public class ManagerNotificationController {

    @Autowired
    private NotificationService notificationService;


    @RequestMapping("/toAdd")
    public String toAdd(){
        return "manage/notification/add-notice";
    }

    @RequestMapping("/update")
    @ResponseBody
    public ResultDTO update(Integer messageId, Integer userId, String reason, Integer status){
        notificationService.update(messageId,userId,reason,status);
        return ResultDTO.okOf();
    }
}
