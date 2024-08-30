package com.server.bbo_gak.domain.auth.service.oauth;

import static com.server.bbo_gak.domain.user.entity.OauthProvider.KAKAO;
import static com.server.bbo_gak.global.config.oauth.GoogleOAuthConfig.AUTHORIZATION;
import static com.server.bbo_gak.global.error.exception.ErrorCode.AUTH_GET_USER_INFO_FAILED;

import com.server.bbo_gak.domain.auth.dto.response.oauth.GoogleTokenServiceResponse;
import com.server.bbo_gak.domain.auth.dto.response.oauth.KakaoOAuthUserInfoResponse;
import com.server.bbo_gak.domain.auth.dto.response.oauth.OauthUserInfoResponse;
import com.server.bbo_gak.global.config.oauth.KakaoOAuthConfig;
import com.server.bbo_gak.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService implements OauthService {

    private final KakaoOAuthConfig kakaoOAuthConfig;

    @Override
    public OauthUserInfoResponse getOauthUserInfo(String accessToken) {
        KakaoOAuthUserInfoResponse response = getKakaoOauthUserInfo(accessToken);
        return new OauthUserInfoResponse(response.id().toString(), response.email(), response.nickname(), KAKAO);
    }

    private KakaoOAuthUserInfoResponse getKakaoOauthUserInfo(String accessToken) {
        try {
            RestClient restClient = RestClient.create();
            return restClient.get()
                .uri(KakaoOAuthConfig.KAKAO_USER_INFO_URI)
                .header(AUTHORIZATION, KakaoOAuthConfig.TOKEN_PREFIX + accessToken)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                    (googleRequest, googleResponse) -> {
                        throw new BusinessException("Client error: " + googleResponse.getStatusCode(),
                            AUTH_GET_USER_INFO_FAILED);
                    })
                .onStatus(HttpStatusCode::is5xxServerError, (googleRequest, googleResponse) -> {
                    throw new BusinessException("Server error: " + googleResponse.getStatusCode(),
                        AUTH_GET_USER_INFO_FAILED);
                })
                .body(KakaoOAuthUserInfoResponse.class);
        } catch (RestClientException e) { // RestClient 관련 에러
            throw new BusinessException("RestClientException: " + e.getMessage(), AUTH_GET_USER_INFO_FAILED);
        } catch (Exception e) { // 그 외 일반적인 예외
            throw new BusinessException("Unexpected error: " + e.getMessage(), AUTH_GET_USER_INFO_FAILED);
        }


    }

    // 프론트와 연결끝나면 지워도 됨.
    public String getKakaoToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", kakaoOAuthConfig.getKakaoClientId());
        params.add("client_secret", kakaoOAuthConfig.getKakaoClientSecret());
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", kakaoOAuthConfig.getKakaoRedirectUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // 요청 URL 설정
        String url = KakaoOAuthConfig.KAKAO_TOKEN_URI;

        // POST 요청 전송 및 응답 수신
        ResponseEntity<GoogleTokenServiceResponse> responseEntity = restTemplate.postForEntity(url, requestEntity,
            GoogleTokenServiceResponse.class);

        // 응답 검증
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            GoogleTokenServiceResponse response = responseEntity.getBody();
            assert response != null;
            return response.accessToken();
        } else {
            // 오류 처리 로직 추가
            throw new RuntimeException("Failed to retrieve token: " + responseEntity.getStatusCode());
        }
    }
}
