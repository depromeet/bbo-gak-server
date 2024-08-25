package com.server.bbo_gak.domain.auth.service.oauth;

import com.server.bbo_gak.domain.auth.dto.response.oauth.OauthUserInfoResponse;

public interface OauthService {

    OauthUserInfoResponse getOauthUserInfo(String accessToken);
}
