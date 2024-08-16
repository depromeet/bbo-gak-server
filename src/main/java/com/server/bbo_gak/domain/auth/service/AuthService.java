package com.server.bbo_gak.domain.auth.service;

import com.server.bbo_gak.domain.auth.dto.request.LoginRequest;
import com.server.bbo_gak.domain.auth.dto.request.RefreshTokenRequest;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.security.jwt.dto.TokenDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    TokenDto login(LoginRequest request);

    TokenDto validateRefreshToken(RefreshTokenRequest request);

    void logout(User user);

}
