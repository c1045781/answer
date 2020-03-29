package top.qiyoung.answer.utils;

import top.qiyoung.answer.dto.NotificationDTO;
import top.qiyoung.answer.model.Notification;

import java.util.Comparator;

public class NotificationByCreateTime implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        NotificationDTO notificationDTO = (NotificationDTO) o1;
        NotificationDTO notificationDTO1 = (NotificationDTO) o2;
        if (notificationDTO.getCreateTime().getTime() < notificationDTO1.getCreateTime().getTime()){
            return 1;
        }else if (notificationDTO.getCreateTime().getTime() > notificationDTO1.getCreateTime().getTime()){
            return -1;
        }else {
            return 1;
        }
    }
}
