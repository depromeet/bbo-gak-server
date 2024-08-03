package com.server.bbo_gak.global.security.jwt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.domain.user.entity.UserRole;
import com.server.bbo_gak.global.security.jwt.dto.TokenDto;
import com.server.bbo_gak.global.security.jwt.entity.RefreshToken;
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

    @Test
    void 토큰_재생성() {
        User user = new User("testUser", "email", UserRole.USER);
        setField(user, "id", 1L);
        TokenDto tokenDto = new TokenDto("newAccessToken", "newRefreshToken");

        when(jwtUtil.generateAccessToken(anyLong(), any(UserRole.class))).thenReturn("newAccessToken");
        when(jwtUtil.generateRefreshToken(anyLong(), any(UserRole.class))).thenReturn("newRefreshToken");

        TokenDto result = jwtTokenService.recreateTokenDtoAtInvalidate(user);

        verify(refreshTokenRepository, times(1)).deleteById(user.getId());
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
        assertEquals(tokenDto.accessToken(), result.accessToken());
        assertEquals(tokenDto.refreshToken(), result.refreshToken());
    }

    @Test
    void token_dto_생성() {
        Long memberId = 1L;
        UserRole role = UserRole.USER;
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        when(jwtUtil.generateAccessToken(anyLong(), any(UserRole.class))).thenReturn(accessToken);
        when(jwtUtil.generateRefreshToken(anyLong(), any(UserRole.class))).thenReturn(refreshToken);

        TokenDto tokenDto = jwtTokenService.createTokenDto(memberId, role);

        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
        assertEquals(accessToken, tokenDto.accessToken());
        assertEquals(refreshToken, tokenDto.refreshToken());
    }
}
