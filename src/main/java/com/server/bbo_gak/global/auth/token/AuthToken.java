package com.server.bbo_gak.global.auth.token;

import com.vivid.apiserver.global.error.exception.AccessDeniedException;
import com.vivid.apiserver.global.error.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {

    @Getter
    private final String token;

    private final Key key;

    @Builder
    public AuthToken(Claims claims, Long duration, Key key) {
        this.key = key;
        this.token = createAuthToken(claims, duration);
    }

    private String createAuthToken(Claims claims, Long duration) {

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + duration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken() {

        return this.getTokenClaims() != null;
    }

    public Claims getTokenClaims() {
        try {

            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException expiredJwtException) {
            throw expiredJwtException;
        } catch (Exception exception) {
            throw new AccessDeniedException(ErrorCode.ACCESS_TOKEN_INVALID);
        }
    }

    /**
     * 토큰 만료여부와 관계없이 subject인 email get
     */
    public String getEmail() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException expiredJwtException) {

            return expiredJwtException.getClaims().getSubject();
        }
    }
}
