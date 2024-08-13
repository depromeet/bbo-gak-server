package com.server.bbo_gak.domain.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

    RECRUIT("공고 마감");

    private final String typeName;
}
