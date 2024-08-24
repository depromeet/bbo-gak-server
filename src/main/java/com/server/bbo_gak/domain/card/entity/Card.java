package com.server.bbo_gak.domain.card.entity;

import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
@SQLDelete(sql = "UPDATE card SET deleted = true WHERE card_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;

    private String title;

    private String content;

    private LocalDateTime accessTime;

    private Boolean copyFlag = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
    private List<CardTag> cardTagList = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CardImage> cardImageList = new ArrayList<>();

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
    private List<CardMemo> cardMemoList = new ArrayList<>();

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
    private List<CardType> cardTypeList = new ArrayList<>();

    @OneToOne(mappedBy = "card", fetch = FetchType.LAZY)
    @JoinColumn(name = "card_copy_info_id")
    private CardCopyInfo cardCopyInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;

    @Builder
    public Card(String title, String content, LocalDateTime accessTime, List<CardType> cardTypeList, User user,
        Boolean copyFlag, Recruit recruit) {
        this.title = title;
        this.content = content;
        this.accessTime = accessTime;
        this.cardTypeList = cardTypeList;
        this.copyFlag = copyFlag;
        this.user = user;
        this.recruit = recruit;
    }

    public static Card creatEmptyCard(User user) {
        return Card.builder()
            .title("")
            .content("")
            .user(user)
            .copyFlag(false)
            .accessTime(LocalDateTime.now())
            .build();
    }

    public static Card copyCardFromMyInfo(Card card, User user, Recruit recruit) {
        return Card.builder()
            .title(card.getTitle())
            .content(card.getContent())
            .user(user)
            .copyFlag(true)
            .accessTime(LocalDateTime.now())
            .recruit(recruit)
            .build();
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateCardTypeList(List<CardType> cardTypeList) {
        this.cardTypeList = cardTypeList;
    }


}
