package com.server.bbo_gak.domain.user.entity;

import com.server.bbo_gak.global.common.BaseEntity;
import jakarta.persistence.*;
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

    @AttributeOverrides({
            @AttributeOverride(name = "oauthId", column = @Column(name = "oauth_id")),
            @AttributeOverride(name = "name", column = @Column(name = "name")),
            @AttributeOverride(name = "email", column = @Column(name = "email")),
            @AttributeOverride(name = "provider", column = @Column(name = "provider"))
    })
    @Embedded
    private OauthInfo oauthInfo;


}
