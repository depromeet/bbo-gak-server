package com.server.bbo_gak.domain.recruit.entity;

import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_id")
    private Long id;

    private String title;

    private String siteUrl;

    @Enumerated(EnumType.STRING)
    private RecruitStatus recruitStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_season_id")
    private Season season;

    @OneToMany(mappedBy = "recruit")
    private List<Card> cardList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Recruit(String title, Season season, String siteUrl, RecruitStatus recruitStatus, List<Card> cardList,
        User user) {
        this.title = title;
        this.season = season;
        this.siteUrl = siteUrl;
        this.recruitStatus = recruitStatus;
        this.cardList = cardList;
        this.user = user;
    }
}
