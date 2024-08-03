package com.server.bbo_gak.domain.user.entity;

import com.server.bbo_gak.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Embedded
    private OauthInfo oauthInfo;


}
