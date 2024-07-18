package com.server.bbo_gak.global.security.jwt.dto;

public record RefreshTokenDto(
    Long memberId,
    String tokenValue
) {

}
