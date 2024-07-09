package com.server.bbo_gak.global.security.jwt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class RefreshToken {

    @Id
    @Column(nullable = false, name = "ID")
    private Long memberId;
    @Column(nullable = false)
    private String token;

    @Builder
    public RefreshToken(Long memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }
}
