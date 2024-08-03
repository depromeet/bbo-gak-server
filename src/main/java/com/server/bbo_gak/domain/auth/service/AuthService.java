package com.server.bbo_gak.domain.auth.service;

import com.server.bbo_gak.domain.auth.dto.request.LoginRequest;
import com.server.bbo_gak.domain.auth.dto.response.LoginResponse;
import com.server.bbo_gak.domain.user.entity.OauthProvider;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.security.jwt.dto.TokenDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    LoginResponse socialLogin(String socialAccessToken, OauthProvider provider);

    TokenDto login(LoginRequest request);

    void logout(User user);

}
