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

    @Embedded
    private OauthInfo oauthInfo;

    // User 생성 팩토리 메서드
    public static User from(OauthInfo oauthInfo) {
        return User.builder()
                .role(UserRole.USER)
                .oauthInfo(oauthInfo)
                .build();
    }
}
