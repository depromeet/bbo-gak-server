package com.server.bbo_gak.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Job {
    ALL("공통"),
    DESIGNER("디자이너"),
    DEVELOPER("개발자"),
    UNDEFINED("미설정");

    private final String value;
}
