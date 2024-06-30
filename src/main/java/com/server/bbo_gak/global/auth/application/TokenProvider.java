package com.server.bbo_gak.global.auth.application;

import com.vivid.apiserver.domain.user.domain.Role;
import com.vivid.apiserver.global.auth.token.AuthToken;
import com.vivid.apiserver.global.error.exception.ErrorCode;
import com.vivid.apiserver.global.error.exception.InvalidValueException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Slf4j
@RequiredArgsConstructor
public class TokenProvider {

    private final Key key;

    private final Long ACCESS_TOKEN_PERIOD = 1000L * 60L * 120L;    // 2시간

    private final Long REFRESH_TOKEN_ERIOD = 1000L * 60L * 60L * 24L * 14L; // 14일

    private static final String AUTHORITIES_KEY = "role";

    public TokenProvider(String secretKey) {
        this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKey.getBytes()));
    }

    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    public AuthToken generateToken(String email, String role, boolean isAccessToken) {

        Claims claims = Jwts.claims().setSubject(email);
        claims.put(AUTHORITIES_KEY, role);

        Long duration = isAccessToken ? ACCESS_TOKEN_PERIOD : REFRESH_TOKEN_ERIOD;

        return AuthToken.builder()
                .claims(claims)
                .key(key)
                .duration(duration)
                .build();
    }

    public Authentication getAuthentication(AuthToken authToken) {

        if (!authToken.validateToken()) {
            throw new InvalidValueException(ErrorCode.ACCESS_TOKEN_INVALID);
        }

        Claims claims = authToken.getTokenClaims();
        Collection<? extends GrantedAuthority> authorities = getGrantedAuthorities(claims);
        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(Claims claims) {

        Role role = Role.valueOf((String) claims.get("role"));

        return Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                .map(c -> new SimpleGrantedAuthority(role.getKey()))
                .collect(Collectors.toList());
    }
}
