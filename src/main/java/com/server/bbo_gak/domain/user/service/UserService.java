package com.server.bbo_gak.domain.user.service;

import com.server.bbo_gak.domain.auth.dto.response.oauth.OauthUserInfoResponse;
import com.server.bbo_gak.domain.user.entity.OauthInfo;
import com.server.bbo_gak.domain.user.entity.User;
import java.util.Optional;

public interface UserService {

    User createUser(OauthUserInfoResponse oauthUserInfo);

    void updateUser();

    void getUser();

    void deleteUser();

    Optional<User> findUserByOauthInfo(OauthInfo oAuthInfo);

}
