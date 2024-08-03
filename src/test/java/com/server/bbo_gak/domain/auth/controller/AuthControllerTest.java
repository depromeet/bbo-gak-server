package com.server.bbo_gak.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.server.bbo_gak.domain.auth.dto.request.LoginRequest;
import com.server.bbo_gak.domain.auth.service.AuthService;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.RestDocsFactory;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import com.server.bbo_gak.global.security.jwt.dto.TokenDto;
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

    private static final String DEFAULT_URL = "/api/v1/users/test";
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
            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL + "/login", loginRequest, HttpMethod.POST,
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

            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL + "/login", invalidLoginRequest, HttpMethod.POST,
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

            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL + "/login", invalidLoginRequest, HttpMethod.POST,
                    objectMapper))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[로그인] 아이디_없음", "auth", invalidLoginRequest));

        }
    }

}
