package com.server.bbo_gak.global.security.jwt.filter;

import com.server.bbo_gak.global.security.jwt.dto.AccessTokenDto;
import com.server.bbo_gak.global.security.jwt.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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

        if (accessTokenHeaderValue == null) {
            filterChain.doFilter(request, response); //AT null
        }

        if (jwtTokenService.validateAccessToken(accessTokenHeaderValue)) {
            //AT 유효
            accessTokenDto = jwtTokenService.retrieveAccessToken(accessTokenHeaderValue);
            jwtTokenService.setAuthenticationToContext(accessTokenDto.memberId(), accessTokenDto.role());
        }

        filterChain.doFilter(request, response);
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