package com.server.bbo_gak.domain.user.entity;

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

}
