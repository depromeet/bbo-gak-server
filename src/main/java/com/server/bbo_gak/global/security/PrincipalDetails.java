package com.server.bbo_gak.global.security;

import com.server.bbo_gak.domain.user.entity.UserRole;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class PrincipalDetails implements UserDetails {

    private final String loginId;

    private final String password;

    private final Long memberId;

    private final UserRole role;

    @Builder
    public PrincipalDetails(String loginId, String password, Long memberId, UserRole role) {
        this.loginId = loginId;
        this.password = password;
        this.memberId = memberId;
        this.role = role;
    }

    public static PrincipalDetails ofJwt(Long memberId, UserRole role) {
        return PrincipalDetails.builder()
            .memberId(memberId)
            .role(role)
            .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getValue()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getMemberId() {
        return memberId;
    }
}
