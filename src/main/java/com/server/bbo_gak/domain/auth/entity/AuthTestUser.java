package com.server.bbo_gak.domain.auth.entity;

import com.server.bbo_gak.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@DiscriminatorValue("AuthTestUser")
public class AuthTestUser extends User {

    private String loginId;

    private String password;

}