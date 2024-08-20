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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE recruit SET deleted = true WHERE id = ?")
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

    @OneToMany(mappedBy = "recruit")
    private List<RecruitSchedule> scheduleList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Recruit(Season season, String title, String siteUrl, RecruitStatus recruitStatus,
        User user) {
        this.season = season;
        this.title = title;
        this.siteUrl = siteUrl;
        this.recruitStatus = recruitStatus;
        this.user = user;
    }

    public Recruit addSchedule(RecruitSchedule recruitSchedule) {
        recruitSchedule.setRecruit(this);
        this.scheduleList.add(recruitSchedule);
        return this;
    }

    public RecruitSchedule getNearestSchedule() {
        return this.scheduleList.stream()
            .filter(schedule -> schedule.getDeadLine().isAfter(LocalDate.now()))
            .min(Comparator.comparing(RecruitSchedule::getDeadLine))
            .orElse(null);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void updateRecruitStatus(RecruitStatus recruitStatus) {
        this.recruitStatus = recruitStatus;
    }

    public void updateSeason(Season season) {
        this.season = season;
    }
}
