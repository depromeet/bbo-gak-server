package com.server.bbo_gak.domain.auth.dto.response.oauth;

import com.server.bbo_gak.domain.user.entity.OauthProvider;

public record OauthUserInfoResponse(
    String oauthId,
    String email,
    String name,
    OauthProvider provider

) {

}
