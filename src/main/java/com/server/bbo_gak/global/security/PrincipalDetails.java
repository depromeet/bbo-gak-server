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


    private final Long userId;

    private final UserRole role;

    @Builder
    public PrincipalDetails(Long userId, UserRole role) {
        this.userId = userId;
        this.role = role;
    }

    public static PrincipalDetails ofJwt(Long userId, UserRole role) {
        return PrincipalDetails.builder()
            .userId(userId)
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
        return null;
    }

    @Override
    public String getUsername() {
        return userId.toString();
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

}
