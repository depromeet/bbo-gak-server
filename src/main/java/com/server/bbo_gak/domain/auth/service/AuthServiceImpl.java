package com.server.bbo_gak.domain.auth.service;

import com.server.bbo_gak.domain.auth.dto.request.LoginRequest;
import com.server.bbo_gak.domain.auth.dto.response.LoginResponse;
import com.server.bbo_gak.domain.auth.dto.response.oauth.OauthUserInfoResponse;
import com.server.bbo_gak.domain.auth.dto.request.RefreshTokenRequest;
import com.server.bbo_gak.domain.auth.entity.AuthTestUser;
import com.server.bbo_gak.domain.auth.entity.AuthTestUserRepository;
import com.server.bbo_gak.domain.auth.service.oauth.GoogleService;
import com.server.bbo_gak.domain.user.entity.OauthProvider;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.domain.user.service.UserService;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.InvalidValueException;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import com.server.bbo_gak.global.security.jwt.dto.AccessTokenDto;
import com.server.bbo_gak.global.security.jwt.dto.TokenDto;
import com.server.bbo_gak.global.security.jwt.entity.RefreshTokenRepository;
import com.server.bbo_gak.global.security.jwt.service.JwtTokenService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthTestUserRepository authTestUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenService jwtTokenService;
    private final GoogleService googleService;
    private final UserService userService;

    @Override
    @Transactional
    public LoginResponse socialLogin(String socialAccessToken, OauthProvider provider) {
        // accessToken으로 사용자 정보 얻어오기
        OauthUserInfoResponse oauthUserInfo = getMemberInfo(socialAccessToken, provider);

        // DB에서 회원 찾기
        User user = userService.findUserByOauthInfo(oauthUserInfo.toEntity())
                .orElseGet(() -> userService.createUser(oauthUserInfo)); //DB에 회원이 없으면 회원가입


        if (refreshTokenRepository.existsRefreshTokenByMemberId(user.getId())) {
            refreshTokenRepository.deleteById(user.getId()); //기존 토큰 삭제
        }
        TokenDto tokenDto = jwtTokenService.createTokenDto(user.getId(), user.getRole()); // 토큰 발급

        return LoginResponse.of(tokenDto);
    }

    @Override
    @Transactional
    public TokenDto login(LoginRequest request) {
        AuthTestUser authTestUser = authTestUserRepository.findByLoginId(request.loginId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!request.password().equals(authTestUser.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCHES);
        }
        if (refreshTokenRepository.existsRefreshTokenByMemberId(authTestUser.getId())) {
            refreshTokenRepository.deleteById(authTestUser.getId());
        }
        return jwtTokenService.createTokenDto(authTestUser.getId(), authTestUser.getRole());
    }

    @Override
    public TokenDto validateRefreshToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();
        if (refreshToken == null) {
            throw new JwtException(ErrorCode.RT_NOT_FOUND.getMessage()); //AT 만료 RT null
        }

        TokenDto tokenDto = null;
        if (jwtTokenService.validateRefreshToken(refreshToken)) {
            // RT 유효
            tokenDto = jwtTokenService.recreateTokenDtoAtValidate(refreshToken);

            AccessTokenDto accessTokenDto = jwtTokenService.retrieveAccessToken(tokenDto.accessToken());
            jwtTokenService.setAuthenticationToContext(accessTokenDto.memberId(), accessTokenDto.role());

        }

        return tokenDto;
    }

    @Override
    @Transactional
    public void logout(User user) {
        if (refreshTokenRepository.existsRefreshTokenByMemberId(user.getId())) {
            refreshTokenRepository.deleteById(user.getId());
        }
    }

    private OauthUserInfoResponse getMemberInfo(String socialAccessToken, OauthProvider provider) {
        return switch (provider) {
            case GOOGLE -> googleService.getOauthUserInfo(socialAccessToken);
            default -> throw new InvalidValueException(ErrorCode.INVALID_PROVIDER_TYPE);
        };
    }
}
