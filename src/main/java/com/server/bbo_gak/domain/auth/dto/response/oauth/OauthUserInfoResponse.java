package com.server.bbo_gak.domain.auth.dto.response.oauth;

import com.server.bbo_gak.domain.user.entity.OauthInfo;
import com.server.bbo_gak.domain.user.entity.OauthProvider;
import lombok.Builder;

@Builder
public record OauthUserInfoResponse(
    String oauthId,
    String email,
    String name,
    OauthProvider provider

) {
    public OauthInfo toEntity() {
        return OauthInfo.builder()
                .oauthId(oauthId)
                .name(name)
                .email(email)
                .provider(provider)
                .build();
    }
}
