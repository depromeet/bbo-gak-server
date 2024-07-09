package com.server.bbo_gak.global.security.jwt.filter;

import com.server.bbo_gak.global.error.exception.ErrorCode;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            String message = ex.getMessage();
            logger.error(ex);
            if (ErrorCode.UNKNOWN_ERROR.getMessage().equals(message)) {
                setResponse(response, ErrorCode.UNKNOWN_ERROR);
            }
            //잘못된 타입의 토큰인 경우
            else if (ErrorCode.WRONG_TYPE_TOKEN.getMessage().equals(message)) {
                setResponse(response, ErrorCode.WRONG_TYPE_TOKEN);
            }
            //Access 토큰 만료된 경우
            else if (ErrorCode.EXPIRED_ACCESS_TOKEN.getMessage().equals(message)) {
                setResponse(response, ErrorCode.EXPIRED_ACCESS_TOKEN);
            }
            //Refresh 토큰 만료된 경우
            else if (ErrorCode.EXPIRED_REFRESH_TOKEN.getMessage().equals(message)) {
                setResponse(response, ErrorCode.EXPIRED_REFRESH_TOKEN);
            }
            //지원되지 않는 토큰인 경우
            else if (ErrorCode.UNSUPPORTED_TOKEN.getMessage().equals(message)) {
                setResponse(response, ErrorCode.UNSUPPORTED_TOKEN);
            } else {
                setResponse(response, ErrorCode.ACCESS_DENIED);
            }
        } catch (NumberFormatException ex) {
            setResponse(response, ErrorCode.TOKEN_SUBJECT_FORMAT_ERROR);
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws RuntimeException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getStatus().value());
        response.getWriter().print(errorCode.getMessage());
    }
}
