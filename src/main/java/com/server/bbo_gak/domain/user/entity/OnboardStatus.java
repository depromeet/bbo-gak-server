package com.server.bbo_gak.domain.user.entity;

import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OnboardStatus {
    NOT_STARTED(false),
    COMPLETED(true);

    private final Boolean value; // 온보딩 수행 여부 (true면 온보딩 완료)

    public static OnboardStatus findByValue(Boolean isOnboardComplete) {
        return Arrays.stream(OnboardStatus.values())
            .filter(onboardStatus -> onboardStatus.getValue().equals(isOnboardComplete))
            .findFirst()
            .orElseThrow((() -> new NotFoundException(ErrorCode.ONBOARD_STATUS_NOT_FOUND)));
    }
}
