package com.server.bbo_gak.domain.user.entity;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    //Optional<User> findUserByEmail(String email);

    Optional<User> findUserByOauthInfo(OauthInfo oauthInfo);
}
