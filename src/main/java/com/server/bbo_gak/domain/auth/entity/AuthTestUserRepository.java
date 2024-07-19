package com.server.bbo_gak.domain.auth.entity;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTestUserRepository extends JpaRepository<AuthTestUser, Long> {

    Optional<AuthTestUser> findByLoginId(String loginId);
}
