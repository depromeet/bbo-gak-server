package com.server.bbo_gak.global.security.jwt.filter;

import com.server.bbo_gak.global.error.exception.ErrorCode;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            setResponse(response, getJwtExceptionErrorCode(ex));
        } catch (NumberFormatException ex) {
            setResponse(response, ErrorCode.TOKEN_SUBJECT_FORMAT_ERROR);
        }
    }

    private ErrorCode getJwtExceptionErrorCode(JwtException ex) {
        ErrorCode[] errorCodes = {
            ErrorCode.UNKNOWN_ERROR,
            ErrorCode.WRONG_TYPE_TOKEN,
            ErrorCode.EXPIRED_ACCESS_TOKEN,
            ErrorCode.EXPIRED_REFRESH_TOKEN,
            ErrorCode.UNSUPPORTED_TOKEN
        };

        return Arrays.stream(errorCodes)
            .filter(errorCode -> errorCode.getMessage().equals(ex.getMessage()))
            .findFirst()
            .orElse(ErrorCode.ACCESS_DENIED);
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) {
        try {
            logger.error(errorCode.getMessage());
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(errorCode.getStatus().value());
            response.getWriter().print(errorCode.getMessage());
        } catch (IOException e) {
            logger.error("Error setting response : ", e);
        }
    }
}
