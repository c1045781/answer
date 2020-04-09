package top.qiyoung.answer.controller.user;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.qiyoung.answer.dto.ExerciseEditDTO;
import top.qiyoung.answer.dto.NotificationDTO;
import top.qiyoung.answer.dto.PaginationDTO;
import top.qiyoung.answer.dto.ResultDTO;
import top.qiyoung.answer.enums.NotificationTypeEnum;
import top.qiyoung.answer.model.Answer;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.model.Notification;
import top.qiyoung.answer.service.AnswerService;
import top.qiyoung.answer.service.ExerciseService;
import top.qiyoung.answer.service.NotificationService;
import top.qiyoung.answer.service.UserService;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/user/notification")
public class UserNotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private UserService userService;

    @RequestMapping("/getLikeList")
    @ResponseBody
    public PaginationDTO getLikeList(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PaginationDTO<NotificationDTO> paginationDTO = notificationService.getList(currentPage, 10, userDetails, NotificationTypeEnum.NOTIFICATION_LIKE.getType());
        return paginationDTO;
    }

    @RequestMapping("/getReplyList")
    @ResponseBody
    public PaginationDTO getReplyList(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PaginationDTO<NotificationDTO> paginationDTO = notificationService.getList(currentPage, 10, userDetails, NotificationTypeEnum.NOTIFICATION_COMMENT.getType());
        return paginationDTO;
    }

    @RequestMapping("/getSystemList")
    @ResponseBody
    public PaginationDTO getSystemList(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,Principal principal){
        PaginationDTO<NotificationDTO> paginationDTO = notificationService.getSystemList(currentPage, 10, principal.getName());
        return paginationDTO;
    }

    @RequestMapping("/view/{id}")
    public String viewNotification(@PathVariable("id") Integer notificationId, Model model){
        Notification notification = notificationService.getNotificationByNotificationId(notificationId);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MyUser user = userService.getUserByUserDetails(userDetails);
        Answer answer = answerService.findAnswerByExerciseIdAndUserId(notification.getOuterId(),user.getUserId());
        ExerciseEditDTO exerciseEditDTO;
        if (answer == null){
            exerciseEditDTO = exerciseService.getExerciseEdit(notification.getOuterId());
        }else {
            exerciseEditDTO = exerciseService.getExerciseEdit(answer.getExerciseId());
            model.addAttribute("answer",answer);
        }
        List<ExerciseEditDTO> exerciseEditDTOList = new ArrayList();
        exerciseEditDTOList.add(exerciseEditDTO);
        model.addAttribute("exerciseEditDTOList", exerciseEditDTOList);
        return "user/answer";
    }

    @RequestMapping("/chat")
    @ResponseBody
    public Set<NotificationDTO> chat(Principal principal){
        Set<NotificationDTO> set = notificationService.chat(principal);
        return set;
    }

    @RequestMapping("/showChat")
    @ResponseBody
    public List<NotificationDTO> showChat(Integer secondId,Principal principal){
        List<NotificationDTO> list = notificationService.showChat(principal,secondId);
        return list;
    }

    @RequestMapping("/newMessage")
    @ResponseBody
    public List<NotificationDTO> newMessage(String to,String from){
        List<NotificationDTO> notifications = notificationService.newMessage(to,from);
        return notifications;
    }

    @RequestMapping("/message")
    public String message(Model model,Principal principal){
        Map<String,Integer> map = notificationService.getNotificationNum(principal.getName());
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Integer> entry = iterator.next();
            System.out.println(entry.getKey()+"--"+entry.getValue());
            model.addAttribute(entry.getKey(),entry.getValue());
        }
        return "user/message";
    }

    // 获取更新后的通知数
    @RequestMapping("/updateNotification")
    @ResponseBody
    public Map<String,Integer> updateNotification(Principal principal){
        Map<String,Integer> map = notificationService.getNotificationNum(principal.getName());
        return map;
    }

    // 获取总通知数
    @RequestMapping("/allNotification")
    @ResponseBody
    public Integer allNotification(Principal principal){
        Integer count = notificationService.getAllNotification(principal.getName());
        return count;
    }

    // 删除聊天信息
    @RequestMapping("/delChat")
    @ResponseBody
    public ResultDTO delChat(String username,Principal principal){
        notificationService.delChat(username,principal.getName());
        return ResultDTO.okOf();
    }
}
