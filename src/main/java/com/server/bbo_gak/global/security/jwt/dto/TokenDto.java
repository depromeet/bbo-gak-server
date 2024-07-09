package com.server.bbo_gak.global.security.jwt.dto;

import lombok.Builder;

@Builder
public record TokenDto(
    String accessToken,
    String refreshToken
) {

    public static TokenDto of(String accessTokenDto, String refreshTokenDto) {
        return TokenDto.builder()
            .accessToken(accessTokenDto)
            .refreshToken(refreshTokenDto)
            .build();
    }
}
