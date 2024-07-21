package com.server.bbo_gak.domain.auth.service;

import com.server.bbo_gak.domain.auth.dto.request.LoginRequest;
import com.server.bbo_gak.domain.auth.entity.AuthTestUser;
import com.server.bbo_gak.domain.auth.entity.AuthTestUserRepository;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import com.server.bbo_gak.global.security.jwt.dto.TokenDto;
import com.server.bbo_gak.global.security.jwt.entity.RefreshTokenRepository;
import com.server.bbo_gak.global.security.jwt.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthTestUserRepository authTestUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenService tokenService;

    @Override
    public TokenDto login(LoginRequest request) {
        AuthTestUser authTestUser = authTestUserRepository.findByLoginId(request.loginId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!request.password().equals(authTestUser.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCHES);
        }
        if (refreshTokenRepository.existsRefreshTokenByMemberId(authTestUser.getId())) {
            refreshTokenRepository.deleteById(authTestUser.getId());
        }
        return tokenService.createTokenDto(authTestUser.getId(), authTestUser.getRole());
    }

    @Override
    public void logout(User user) {
        if (refreshTokenRepository.existsRefreshTokenByMemberId(user.getId())) {
            refreshTokenRepository.deleteById(user.getId());
        }
    }
}
