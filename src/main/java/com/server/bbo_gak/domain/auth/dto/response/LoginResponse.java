package com.server.bbo_gak.domain.auth.dto.response;

import com.server.bbo_gak.global.security.jwt.dto.TokenDto;
import lombok.Builder;

@Builder
public record LoginResponse(
    String accessToken,
    String refreshToken,
    Boolean isFirstLogin
) {

    public static LoginResponse of(TokenDto tokenDto, Boolean isFirstLogin) {
        return LoginResponse.builder()
            .accessToken(tokenDto.accessToken())
            .refreshToken(tokenDto.refreshToken())
            .isFirstLogin(isFirstLogin)
            .build();
    }
}
