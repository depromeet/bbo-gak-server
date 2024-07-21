package com.server.bbo_gak.domain.auth.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.server.bbo_gak.domain.auth.dto.request.LoginRequest;
import com.server.bbo_gak.domain.auth.entity.AuthTestUser;
import com.server.bbo_gak.domain.auth.entity.AuthTestUserRepository;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.domain.user.entity.UserRole;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import com.server.bbo_gak.global.security.jwt.dto.TokenDto;
import com.server.bbo_gak.global.security.jwt.entity.RefreshTokenRepository;
import com.server.bbo_gak.global.security.jwt.service.JwtTokenService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AuthServiceImplTest {

    @Mock
    private AuthTestUserRepository authTestUserRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 테스트_로그인_유저가_없을때_실패() {
        when(authTestUserRepository.findByLoginId(anyString())).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest("testUser", "password");

        assertThrows(NotFoundException.class, () -> authService.login(request));
    }

    @Test
    void 테스트_로그인_유저_비밀번호_다를때_실패() {
        AuthTestUser authTestUser = new AuthTestUser("name", "email", UserRole.USER, "testUser", "wrongPassword");

        when(authTestUserRepository.findByLoginId(anyString())).thenReturn(Optional.of(authTestUser));

        LoginRequest request = new LoginRequest("testUser", "password");

        assertThrows(BusinessException.class, () -> authService.login(request));
    }

    @Test
    void 테스트_로그인_성공() {
        AuthTestUser authTestUser = new AuthTestUser("name", "email", UserRole.USER, "testUser", "password");

        when(authTestUserRepository.findByLoginId(anyString())).thenReturn(Optional.of(authTestUser));
        when(refreshTokenRepository.existsRefreshTokenByMemberId(anyLong())).thenReturn(false);
        when(jwtTokenService.createTokenDto(anyLong(), any(UserRole.class))).thenReturn(
            new TokenDto("accessToken", "refreshToken"));

        LoginRequest request = new LoginRequest("testUser", "password");

        TokenDto tokenDto = authService.login(request);

        verify(refreshTokenRepository, never()).deleteById(anyLong());
    }

    @Test
    void 테스트_로그아웃_실패() {
        User user = new User("name", "testUser", UserRole.USER);

        when(refreshTokenRepository.existsRefreshTokenByMemberId(anyLong())).thenReturn(true);
        doNothing().when(refreshTokenRepository).deleteById(anyLong());

        authService.logout(user);

    }
}
