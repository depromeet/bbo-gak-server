package com.server.bbo_gak.domain.user.entity;

import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.InvalidValueException;
import java.util.Arrays;

public enum OauthProvider {
    GOOGLE,
    KAKAO;

    public static OauthProvider findByName(String name) {
        return Arrays.stream(OauthProvider.values())
            .filter(oauthProvider -> oauthProvider.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new InvalidValueException(ErrorCode.INVALID_PROVIDER_TYPE));
    }
}
