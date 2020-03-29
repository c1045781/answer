package top.qiyoung.answer.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import top.qiyoung.answer.model.Chat;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.model.Notification;
import top.qiyoung.answer.service.NotificationService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class UserWebSocketController {

    /**
     * 通过SimpMessagingTemplate模板向浏览器发送消息。如果是广播模式，可以直接使用注解@SendTo
     */
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private NotificationService notificationService;

    /**
     * 开启STOMP协议来传输基于代理的消息，这时控制器支持使用@MessageController，就像使用@RequestMapping是一样的
     * 当浏览器向服务端发送请求时，通过@MessageController映射/chat这个路径
     * 在SpringMVC中，可以直接在参数中获得principal,其中包含当前用户的信息
     * @param principal Principal
     * @param chat Chat
     */
    @MessageMapping("/chat")
    public void handleChat(Principal principal, Chat chat) {
        //下面的代码就是如果发送人是Michael，接收人就是Janet，发送的信息是message，反之亦然。
            //通过SimpMessagingTemplate的convertAndSendToUser向用户发送消息。
            //第一参数表示接收信息的用户，第二个是浏览器订阅的地址，第三个是消息本身
        notificationService.addNotification(principal.getName(),chat.getTo(),chat.getContent());
        chat.setFrom(principal.getName());
        simpMessagingTemplate.convertAndSendToUser(chat.getTo(),"/queue/notifications", chat);
        simpMessagingTemplate.convertAndSendToUser(principal.getName(),"/queue/notifications", chat);
    }
}
