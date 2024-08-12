package com.server.bbo_gak.domain.notification.controller;

import com.server.bbo_gak.domain.notification.dto.response.NotificationGetNumResponse;
import com.server.bbo_gak.domain.notification.dto.response.NotificationGetResponse;
import com.server.bbo_gak.domain.notification.service.NotificationService;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.annotation.AuthUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/num")
    public ResponseEntity<NotificationGetNumResponse> getNotificationNum(
        @AuthUser User user
    ) {
        return ResponseEntity.ok(notificationService.getNotificationNum(user));
    }

    @GetMapping("")
    public ResponseEntity<List<NotificationGetResponse>> getNotificationList(
        @AuthUser User user
    ) {
        return ResponseEntity.ok(notificationService.getNotificationList(user));
    }

    @PutMapping("")
    public ResponseEntity<Void> updateNotificationReadToTrue(
        @AuthUser User user
    ) {
        notificationService.updateAllNotificationToRead(user);
        return ResponseEntity.ok().body(null);
    }
}
