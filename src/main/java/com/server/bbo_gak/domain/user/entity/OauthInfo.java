package com.server.bbo_gak.domain.user.entity;

import com.server.bbo_gak.domain.auth.dto.response.oauth.OauthUserInfoResponse;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OauthInfo {

    private String oauthId;
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private OauthProvider provider;

    public static OauthInfo from(OauthUserInfoResponse oauthUserInfo) {
        return OauthInfo.builder()
                .oauthId(oauthUserInfo.oauthId())
                .name(oauthUserInfo.name())
                .email(oauthUserInfo.email())
                .provider(oauthUserInfo.provider())
                .build();
    }

}
