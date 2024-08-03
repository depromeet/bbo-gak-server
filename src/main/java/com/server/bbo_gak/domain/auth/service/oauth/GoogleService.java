package com.server.bbo_gak.domain.auth.service.oauth;


import static com.server.bbo_gak.domain.user.entity.OauthProvider.GOOGLE;
import static com.server.bbo_gak.global.config.oauth.GoogleOAuthConfig.AUTHORIZATION;
import static com.server.bbo_gak.global.config.oauth.GoogleOAuthConfig.GOOGLE_TOKEN_URI;
import static com.server.bbo_gak.global.config.oauth.GoogleOAuthConfig.GOOGLE_USER_INFO_URI;
import static com.server.bbo_gak.global.config.oauth.GoogleOAuthConfig.TOKEN_PREFIX;
import static com.server.bbo_gak.global.error.exception.ErrorCode.AUTH_GET_USER_INFO_FAILED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.server.bbo_gak.domain.auth.dto.response.oauth.GoogleOauthUserInfoResponse;
import com.server.bbo_gak.domain.auth.dto.response.oauth.GoogleTokenServiceResponse;
import com.server.bbo_gak.domain.auth.dto.response.oauth.OauthUserInfoResponse;
import com.server.bbo_gak.global.config.oauth.GoogleOAuthConfig;
import com.server.bbo_gak.global.error.exception.BusinessException;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;


@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleService implements OauthService {

    private final GoogleOAuthConfig googleOAuthConfig;

    @Override
    public OauthUserInfoResponse getOauthUserInfo(String accessToken) {
        GoogleOauthUserInfoResponse response = null;
        try {
            RestClient restClient = RestClient.create();
            response = restClient.get()
                .uri(GOOGLE_USER_INFO_URI)
                .header(AUTHORIZATION, TOKEN_PREFIX + accessToken)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                    (googleRequest, googleResponse) -> {
                        throw new BusinessException(AUTH_GET_USER_INFO_FAILED);
                    })
                .body(GoogleOauthUserInfoResponse.class);
        } catch (Exception e) {
            throw new BusinessException(AUTH_GET_USER_INFO_FAILED);
        }

        assert response != null;
        log.info("getOAuthUserInfo = oauthId {} / email {} / name {}", response.id(), response.email(),
            response.name());
        return new OauthUserInfoResponse(response.id(), response.email(), response.name(), GOOGLE);
    }

    // 프론트와 연결끝나면 지워도 됨.
    public String getToken(String code) {
//        log.info("redirect uri: {}", googleOAuthConfig.getGoogleRedirectUri());
        // 토큰 요청 데이터
        String uri = UriComponentsBuilder.fromOriginHeader(GOOGLE_TOKEN_URI)
            .toUriString();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("client_id", googleOAuthConfig.getGoogleClientId());
        params.put("client_secret", googleOAuthConfig.getGoogleClientSecret());
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", googleOAuthConfig.getGoogleRedirectUri());

        GoogleTokenServiceResponse response = RestClient.create(GOOGLE_TOKEN_URI)
            .post()
            .contentType(APPLICATION_JSON)
            .body(params)
            .retrieve()
            .body(GoogleTokenServiceResponse.class);

        assert response != null;
        return response.accessToken();
    }
}
