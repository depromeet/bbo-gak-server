package com.server.bbo_gak.global.security.jwt.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.server.bbo_gak.global.security.jwt.entity.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JwtTokenServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private JwtTokenService jwtTokenService;

    @Test
    public void 유효한_토큰_통과() {
        String token = "validRefreshToken";
        Jws<Claims> claimsJws = mock(Jws.class);
        Claims claims = mock(Claims.class);

        when(jwtUtil.getRefreshTokenClaims(token)).thenReturn(claimsJws);
        when(claimsJws.getBody()).thenReturn(claims);
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 100000)); // 유효한 만료 시간

        boolean result = jwtTokenService.validateRefreshToken(token);

        assertTrue(result);
        verify(jwtUtil, times(1)).getRefreshTokenClaims(token);
    }
}
