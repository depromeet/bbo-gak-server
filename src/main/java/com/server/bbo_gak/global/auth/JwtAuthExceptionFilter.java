package com.server.bbo_gak.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivid.apiserver.global.error.ErrorResponse;
import com.vivid.apiserver.global.error.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthExceptionFilter extends OncePerRequestFilter {

    //인증 오류가 아닌, JWT 관련 오류는 이 필터에서 따로 잡아냄.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // move to next filter
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException expiredJwtException) {

            // access token 만료 exception
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, ErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (Exception jwtException) {

            // access token invalid exception
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, ErrorCode.ACCESS_TOKEN_INVALID);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, ErrorCode errorCode)
            throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.from(errorCode)));
    }
}
