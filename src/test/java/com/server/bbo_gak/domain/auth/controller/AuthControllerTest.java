package com.server.bbo_gak.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.server.bbo_gak.domain.auth.dto.request.LoginRequest;
import com.server.bbo_gak.domain.auth.dto.request.RefreshTokenRequest;
import com.server.bbo_gak.domain.auth.service.AuthService;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.RestDocsFactory;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import com.server.bbo_gak.global.security.jwt.dto.TokenDto;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AuthControllerTest extends AbstractRestDocsTests {

    private static final String DEFAULT_URL = "/api/v1/users";
    @MockBean
    private AuthService authService;

    @Autowired
    private RestDocsFactory restDocsFactory;

    @Nested
    class 로그인 {

        @Test
        public void 성공() throws Exception {
            TokenDto tokenDto = TokenDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

            LoginRequest loginRequest = new LoginRequest("test", "test123");

            when(authService.login(any(LoginRequest.class))).thenReturn(tokenDto);

            //then
            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL + "/test/login", loginRequest, HttpMethod.POST,
                    objectMapper))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResource("[로그인] 성공", "jwt 토큰 생성", "auth", loginRequest, tokenDto));
        }

        @Test
        public void 비밀번호_실패() throws Exception {

            // Invalid request (missing loginId)
            LoginRequest invalidLoginRequest = new LoginRequest("test", "wrong");
            when(authService.login(any(LoginRequest.class))).thenThrow(
                new BusinessException(ErrorCode.PASSWORD_NOT_MATCHES));

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/test/login", invalidLoginRequest, HttpMethod.POST,
                        objectMapper))
                .andExpect(status().isBadRequest())
                .andDo(restDocsFactory.getFailureResource("[로그인] 비밀번호_실패", "auth", invalidLoginRequest));
        }

        @Test
        public void 아이디_없음() throws Exception {

            // Invalid request (missing loginId)
            LoginRequest invalidLoginRequest = new LoginRequest("wrong", "test123");
            when(authService.login(any(LoginRequest.class))).thenThrow(
                new NotFoundException(ErrorCode.USER_NOT_FOUND));

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/test/login", invalidLoginRequest, HttpMethod.POST,
                        objectMapper))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[로그인] 아이디_없음", "auth", invalidLoginRequest));

        }
    }

    @Nested
    class 리프레쉬_토큰 {

        @Test
        public void 성공() throws Exception {

            TokenDto tokenDto = TokenDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

            String validRefreshToken = "validRefreshToken";

            RefreshTokenRequest request = new RefreshTokenRequest(validRefreshToken);

            when(authService.validateRefreshToken(any(RefreshTokenRequest.class))).thenReturn(tokenDto);

            //then
            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL + "/refreshToken", request, HttpMethod.POST,
                    objectMapper))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResource("[리프레쉬 토큰] 유효성 검사 성공", "jwt 토큰 유효성 검사 및 재발급", "auth",
                    request, tokenDto));
        }

        @Test
        public void 실패() throws Exception {

            TokenDto tokenDto = TokenDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

            String validRefreshToken = "invalidRefreshToken";

            RefreshTokenRequest request = new RefreshTokenRequest(validRefreshToken);

            when(authService.validateRefreshToken(any(RefreshTokenRequest.class))).thenThrow(
                new ExpiredJwtException(null, null, "Expired"));
            //then
            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL + "/refreshToken", request, HttpMethod.POST,
                    objectMapper))
                .andExpect(status().isUnauthorized())
                .andDo(restDocsFactory.getFailureResource("[리프레쉬 토큰] 유효성 검사 실패", "auth",
                    request));
        }
    }

    @Nested
    class 로그아웃 {

        @Test
        public void 성공() throws Exception {

            doNothing().when(authService).logout(any(User.class));

            //then
            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/logout", null, HttpMethod.GET, objectMapper))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResource("[로그아웃] 성공", "로그아웃", "auth", null, null));
        }
    }
}
