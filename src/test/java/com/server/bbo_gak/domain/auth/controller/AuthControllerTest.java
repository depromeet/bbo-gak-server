package com.server.bbo_gak.domain.auth.controller;

import static com.server.bbo_gak.global.security.jwt.service.JwtTokenService.TOKEN_ROLE_NAME;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.server.bbo_gak.domain.auth.dto.request.LoginRequest;
import com.server.bbo_gak.domain.auth.dto.request.RefreshTokenRequest;
import com.server.bbo_gak.domain.user.entity.UserRole;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.RestDocsFactory;
import com.server.bbo_gak.global.security.jwt.dto.TokenDto;
import com.server.bbo_gak.global.security.jwt.entity.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Rollback(true)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"/all-data-delete.sql", "/auth-test-data.sql"})
public class AuthControllerTest extends AbstractRestDocsTests {

    private static final String DEFAULT_URL = "/api/v1/users";

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RestDocsFactory restDocsFactory;

    @Value("${jwt.refresh-token-secret}")
    private String jwtRTSecret;

    @Value("${jwt.refresh-token-expiration-time}")
    private Long RTexpiration;

    @Value("${jwt.issuer}")
    private String issuer;

    private String refreshToken;

    @BeforeEach
    void setUp() {
        Date now = new Date();
        Date validity = new Date(now.getTime() + RTexpiration);
        refreshToken = Jwts.builder()
            .setSubject("1")
            .setIssuedAt(now)
            .setExpiration(validity)
            .claim(TOKEN_ROLE_NAME, UserRole.USER.getValue())
            .signWith(Keys.hmacShaKeyFor(jwtRTSecret.getBytes()), SignatureAlgorithm.HS256)
            .setIssuer(issuer)
            .compact();
    }

    @Nested
    class 로그인 {

        @Test
        public void 성공() throws Exception {
            TokenDto tokenDto = TokenDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

            LoginRequest loginRequest = new LoginRequest("test", "test123");
            //then
            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL + "/test/login", loginRequest, HttpMethod.POST,
                    objectMapper))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResource("[로그인] 성공", "jwt 토큰 생성", "auth", loginRequest, tokenDto));

            assertTrue(refreshTokenRepository.findById(1L).isPresent());

        }

        @Test
        public void 비밀번호_실패() throws Exception {

            // Invalid request (missing loginId)
            LoginRequest invalidLoginRequest = new LoginRequest("test", "wrong");

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

            RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

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

            //then
            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/logout", null, HttpMethod.GET, objectMapper))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResource("[로그아웃] 성공", "로그아웃", "auth", null, null));

            assertTrue(refreshTokenRepository.findById(1L).isEmpty());

        }
    }
}
