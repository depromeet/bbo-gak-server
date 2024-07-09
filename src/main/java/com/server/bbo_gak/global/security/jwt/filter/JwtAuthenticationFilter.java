package com.server.bbo_gak.global.security.jwt.filter;

import com.server.bbo_gak.domain.user.entity.UserRole;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.security.PrincipalDetails;
import com.server.bbo_gak.global.security.jwt.dto.AccessTokenDto;
import com.server.bbo_gak.global.security.jwt.dto.TokenDto;
import com.server.bbo_gak.global.security.jwt.service.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_PREFIX = "Bearer ";
    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String accessTokenHeaderValue = extractAccessTokenFromHeader(request);
        String refreshTokenHeaderValue = extractRefreshTokenFromHeader(request);

        AccessTokenDto accessTokenDto;
        //accessToken이 헤더에 존재하면
        if (accessTokenHeaderValue != null) {

            try {
                if (jwtTokenService.validateAccessToken(accessTokenHeaderValue)) {
                    //AccessToken이 유효하므로 SecurityContextHolder에 유저 정보를 저장합니다.
                    accessTokenDto = jwtTokenService.retrieveAccessToken(accessTokenHeaderValue);
                    setAuthenticationToContext(accessTokenDto.memberId(), accessTokenDto.role());
                }
            } catch (ExpiredJwtException e) {
                if (refreshTokenHeaderValue == null) {
                    //Access Token 만료되고 refresh Token이 헤더에 없는 경우
                    throw new JwtException(ErrorCode.AT_EXPIRED_AND_RT_NOT_FOUND.getMessage());
                } else if (jwtTokenService.validateRefreshToken(refreshTokenHeaderValue)) {
                    // 어세스 토큰이 만료된 상황 && 리프레시 토큰 또한 존재하는 상황
                    boolean isRefreshToken = jwtTokenService.existsRefreshToken(refreshTokenHeaderValue);
                    if (isRefreshToken) {
                        TokenDto tokenDto = jwtTokenService.recreateTokenDto(refreshTokenHeaderValue);

                        //Refresh, Access Token을 헤더에 넣어 전송합니다 (GET 요청시에는 body가 없기 때문에)
                        jwtTokenService.setHeaderAccessToken(response, tokenDto.accessToken());
                        jwtTokenService.setHeaderRefreshToken(response, tokenDto.refreshToken());

                        //Refresh Token으로 발급한 AccessToken으로 유저 정보를 저장합니다.
                        accessTokenDto = jwtTokenService.retrieveAccessToken(tokenDto.accessToken());
                        setAuthenticationToContext(accessTokenDto.memberId(), accessTokenDto.role());
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthenticationToContext(Long memberId, UserRole role) {
        UserDetails userDetails = PrincipalDetails.ofJwt(memberId, role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String extractAccessTokenFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
            .filter(header -> header.startsWith(TOKEN_PREFIX))
            .map(header -> header.replace(TOKEN_PREFIX, ""))
            .orElse(null);
    }

    private String extractRefreshTokenFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("RefreshToken"))
            .filter(header -> header.startsWith(TOKEN_PREFIX))
            .map(header -> header.replace(TOKEN_PREFIX, ""))
            .orElse(null);
    }
}