package com.server.bbo_gak.domain.auth.dto.response.oauth;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public record KakaoOAuthUserInfoResponse(
    Long id,
    LocalDateTime connected_at,
    Map<String, Object> properties,
    KakaoAccount kakao_account
) {

    public static KakaoOAuthUserInfoResponse from(Map<String, Object> attributes) {
        return new KakaoOAuthUserInfoResponse(
            Long.valueOf(String.valueOf(attributes.get("id"))),
            LocalDateTime.parse(
                String.valueOf(attributes.get("connected_at")),
                DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())
            ),
            (Map<String, Object>) attributes.get("properties"),
            KakaoAccount.from((Map<String, Object>) attributes.get("kakao_account"))
        );
    }

    public String email() {
        return this.kakao_account().email();
    }

    public String nickname() {
        return this.kakao_account().nickname();
    }

    public record KakaoAccount(
        Boolean profileNicknameNeedsAgreement,
        Profile profile,
        Boolean hasEmail,
        Boolean emailNeedsAgreement,
        Boolean isEmailValid,
        Boolean isEmailVerified,
        String email
    ) {

        public static KakaoAccount from(Map<String, Object> attributes) {
            return new KakaoAccount(
                Boolean.valueOf(String.valueOf(attributes.get("profile_nickname_needs_agreement"))),
                Profile.from((Map<String, Object>) attributes.get("profile")),
                Boolean.valueOf(String.valueOf(attributes.get("has_email"))),
                Boolean.valueOf(String.valueOf(attributes.get("email_needs_agreement"))),
                Boolean.valueOf(String.valueOf(attributes.get("is_email_valid"))),
                Boolean.valueOf(String.valueOf(attributes.get("is_email_verified"))),
                String.valueOf(attributes.get("email"))
            );
        }

        public String nickname() {
            return this.profile().nickname();
        }

        public record Profile(String nickname) {

            public static Profile from(Map<String, Object> attributes) {
                return new Profile(String.valueOf(attributes.get("nickname")));
            }
        }
    }
}
