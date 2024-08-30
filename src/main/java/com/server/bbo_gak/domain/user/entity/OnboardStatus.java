package com.server.bbo_gak.domain.user.entity;

import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OnboardStatus {
    NOT_STARTED("온보딩_미완료"),
    COMPLETED("온보딩_완료");

    private final String value;

    public static OnboardStatus findByValue(String value) {
        return Arrays.stream(OnboardStatus.values())
            .filter(onboardStatus -> onboardStatus.getValue().equals(value))
            .findFirst()
            .orElseThrow((() -> new NotFoundException(ErrorCode.ONBOARD_STATUS_NOT_FOUND)));
    }
}
