package com.server.bbo_gak.domain.notification.service;

import com.server.bbo_gak.domain.notification.dao.NotificationRepository;
import com.server.bbo_gak.domain.notification.dto.response.NotificationGetNumResponse;
import com.server.bbo_gak.domain.notification.dto.response.NotificationGetResponse;
import com.server.bbo_gak.domain.notification.entity.Notification;
import com.server.bbo_gak.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {


    private final NotificationRepository notificationRepository;

    public NotificationGetNumResponse getNotificationNum(User user) {
        return new NotificationGetNumResponse(notificationRepository.countByUserAndIsReadFalse(user));
    }

    public List<NotificationGetResponse> getNotificationList(User user) {

        return notificationRepository.findAllByUserOrderByCreatedDateDesc(user).stream()
            .map(NotificationGetResponse::from)
            .toList();
    }

    @Transactional
    public void updateAllNotificationToRead(User user) {
        List<Notification> notificationList = notificationRepository.findAllByUserAndIsReadFalse(user);
        notificationList.forEach(Notification::updateReadTrue);
        notificationRepository.saveAll(notificationList);
    }
}
