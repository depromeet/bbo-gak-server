package com.server.bbo_gak.domain.auth.dto.request;

public record LoginRequest(
    String loginId,
    String password
) {

}
