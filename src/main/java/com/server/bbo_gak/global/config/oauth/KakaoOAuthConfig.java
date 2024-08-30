package com.server.bbo_gak.global.config.oauth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KakaoOAuthConfig {

    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String KAKAO_CODE_URI = "https://kauth.kakao.com/oauth/authorize";
    public static final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    public static final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    @Value("${kakao.login.client_id}")
    private String kakaoClientId;

    @Value("${kakao.login.client_secret}")
    private String kakaoClientSecret;

    @Value("${kakao.login.redirect_uri}")
    private String kakaoRedirectUri;
}
