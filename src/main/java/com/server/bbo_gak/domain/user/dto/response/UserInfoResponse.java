package com.server.bbo_gak.domain.user.dto.response;

import com.server.bbo_gak.domain.user.entity.UserRole;

public record UserInfoResponse(
    String name,
    String email,
    UserRole role
) {

}
