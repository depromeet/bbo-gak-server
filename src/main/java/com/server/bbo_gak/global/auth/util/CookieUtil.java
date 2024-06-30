package com.server.bbo_gak.global.auth.util;

import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "vivid-at";

    /**
     * access token을 쿠키에서 get하는 메소드
     */
    public static Optional<Cookie> getAccessTokenCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length <= 0) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                return Optional.of(cookie);
            }
        }

        return Optional.empty();
    }

    /**
     * access token을 쿠키에 추가하는 메소드
     */
    public static void addAccessTokenCookie(HttpServletResponse response, String accessToken) {

        // create a cookie
        Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken);

        cookie.setMaxAge(14 * 24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    /**
     * access token을 쿠키에서 삭제하는 메소드
     */
    public static void deleteAccessTokenCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);

                    response.addCookie(cookie);
                }
            }
        }
    }

}
