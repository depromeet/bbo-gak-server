package com.server.bbo_gak.global.security.jwt.dto;


import com.server.bbo_gak.domain.user.entity.UserRole;

public record AccessTokenDto(
    Long memberId,
    UserRole role,
    String tokenValue
) {

}
