package com.server.bbo_gak.domain.notification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.server.bbo_gak.domain.notification.dao.NotificationRepository;
import com.server.bbo_gak.domain.notification.entity.Notification;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
@Sql("/notification-test-data.sql")
public class NotificationSchedulerTest {

    @Autowired
    private NotificationScheduler notificationScheduler;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    public void 자정알림생성_성공() {

        // when: 스케줄러 실행
        notificationScheduler.executeAtMidnight();

        // then: Notification이 잘 생성되었는지 검증
        List<Notification> notifications = notificationRepository.findAll();
        assertEquals(1, notifications.size());
    }
}
