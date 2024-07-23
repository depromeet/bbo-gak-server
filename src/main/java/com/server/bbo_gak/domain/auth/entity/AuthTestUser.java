package com.server.bbo_gak.domain.auth.entity;

import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.domain.user.entity.UserRole;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("AuthTestUser")
public class AuthTestUser extends User {

    private String loginId;

    private String password;

    public AuthTestUser(String name, String email, UserRole role, String loginId, String password) {
        super(name, email, role);
        this.loginId = loginId;
        this.password = password;
    }
}
