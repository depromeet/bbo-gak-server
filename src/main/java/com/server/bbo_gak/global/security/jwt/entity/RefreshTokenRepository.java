package com.server.bbo_gak.global.security.jwt.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsRefreshTokenByMemberId(Long memberId);

    boolean existsRefreshTokenByToken(String token);

}
