package com.server.bbo_gak.global.auth.application;

import com.vivid.apiserver.domain.auth.service.command.RefreshTokenCommandService;
import com.vivid.apiserver.domain.user.domain.Role;
import com.vivid.apiserver.domain.user.dto.request.UserLoginRequest;
import com.vivid.apiserver.domain.user.service.UserManageService;
import com.vivid.apiserver.domain.user.service.query.UserQueryService;
import com.vivid.apiserver.global.auth.token.AuthToken;
import com.vivid.apiserver.global.auth.util.CookieUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RefreshTokenCommandService refreshTokenCommandService;

    @Value("${root-url}")
    private String rootUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        UserLoginRequest userLoginRequest = UserLoginRequest.of(oAuth2User.getAttributes());
        String email = userLoginRequest.getEmail();

        signUpIfNotUser(userLoginRequest);

        AuthToken accessToken = tokenProvider.generateToken(email, Role.USER.name(), true);
        AuthToken refreshToken = tokenProvider.generateToken(email, Role.USER.name(), false);
        CookieUtil.addAccessTokenCookie(response, accessToken.getToken());
        refreshTokenCommandService.save(email, refreshToken.getToken());

        getRedirectStrategy().sendRedirect(request, response, createTargetUrl(userLoginRequest, accessToken));
    }

    private String createTargetUrl(UserLoginRequest userLoginRequest, AuthToken authToken)
            throws UnsupportedEncodingException {
        return UriComponentsBuilder.fromUriString(rootUrl + "/success-login")
                .queryParam("token", authToken.getToken())
                .queryParam("name", URLEncoder.encode(userLoginRequest.getName(), "UTF-8"))
                .queryParam("picture", userLoginRequest.getPicture())
                .build().toUriString();
    }

    /**
     * 최초 로그인이라면 회원가입 처리.
     */
    private void signUpIfNotUser(UserLoginRequest userLoginRequest) {
        if (!userQueryService.isDuplicatedUserByEmail(userLoginRequest.getEmail())) {
            userManageService.signUp(userLoginRequest);
        }
    }
}
