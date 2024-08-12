package com.server.bbo_gak.domain.notification.dto.response;

import com.server.bbo_gak.domain.notification.entity.Notification;
import com.server.bbo_gak.domain.notification.entity.NotificationType;
import com.server.bbo_gak.global.utils.BaseDateTimeFormatter;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record NotificationGetResponse(
    Long id,
    String title,
    String message,
    boolean isRead,
    NotificationType type,
    Long referenceId,
    String createdAt
) {

    public static NotificationGetResponse from(Notification notification) {
        return NotificationGetResponse.builder()
            .id(notification.getId())
            .title(notification.getTitle())
            .message(notification.getMessage())
            .isRead(notification.isRead())
            .type(notification.getType())
            .referenceId(notification.getReferenceId())
            .createdAt(notification.getCreatedDate().format(BaseDateTimeFormatter.getLocalDateTimeFormatter()))
            .build();
    }
}
