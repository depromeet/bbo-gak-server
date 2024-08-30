package com.server.bbo_gak.domain.auth.controller;

import com.server.bbo_gak.domain.auth.dto.request.LoginRequest;
import com.server.bbo_gak.domain.auth.dto.request.RefreshTokenRequest;
import com.server.bbo_gak.domain.auth.dto.response.LoginResponse;
import com.server.bbo_gak.domain.auth.service.AuthService;
import com.server.bbo_gak.domain.auth.service.oauth.GoogleService;
import com.server.bbo_gak.domain.auth.service.oauth.KakaoService;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.annotation.AuthUser;
import com.server.bbo_gak.global.security.jwt.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AuthController {

    private static final String SOCIAL_TOKEN_NAME = "SOCIAL-AUTH-TOKEN";
    private final AuthService authService;
    private final GoogleService googleService;
    private final KakaoService kakaoService;

    @PostMapping("/social-login")
    public ResponseEntity<LoginResponse> socialLogin(
        @RequestHeader(SOCIAL_TOKEN_NAME) final String socialAccessToken,
        @RequestParam(name = "provider") String provider
    ) {
        LoginResponse response = authService.socialLogin(socialAccessToken, provider);
        //TODO: 쿠키 만들어서 헤더에 넘겨야함.
        return ResponseEntity.ok(response);
    }

    /**
     * 프론트 연결 끝나면 지우기
     */
    @PostMapping("/test/google/access-token")
    public String googleTest(@RequestParam("code") String code) {
        return googleService.getGoogleToken(code);
    }

    @PostMapping("/test/kakao/access-token")
    public String kakaoTest(@RequestParam("code") String code) {
        return kakaoService.getKakaoToken(code);
    }

    @PostMapping("/test/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenDto> validateRefreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.validateRefreshToken(request));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@AuthUser User user) {
        authService.logout(user);
        return ResponseEntity.ok().body(null);
    }

}
