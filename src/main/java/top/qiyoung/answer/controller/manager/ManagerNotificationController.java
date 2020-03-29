package top.qiyoung.answer.controller.manager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager/notification")
public class ManagerNotificationController {

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "manage/notification/add-notification";
    }

}
