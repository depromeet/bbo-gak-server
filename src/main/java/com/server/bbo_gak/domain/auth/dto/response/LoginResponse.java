package com.server.bbo_gak.domain.auth.dto.response;

import com.server.bbo_gak.global.security.jwt.dto.TokenDto;
import lombok.Builder;

@Builder
public record LoginResponse(
    String accessToken,
    String refreshToken
) {

    public static LoginResponse of(TokenDto tokenDto) {
        return LoginResponse.builder()
            .accessToken(tokenDto.accessToken())
            .refreshToken(tokenDto.refreshToken())
            .build();
    }
}
