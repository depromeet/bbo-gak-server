package com.server.bbo_gak.domain.card.entity;

import com.server.bbo_gak.domain.recruit.entity.Recruit;
import com.server.bbo_gak.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
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
@SQLDelete(sql = "UPDATE card SET deleted = true WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;

    private String title;

    private String content;

    private LocalDateTime accessTime;

    private Long userId;

    private boolean copyFlag = false;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @OneToMany(mappedBy = "card")
    private List<CardTag> cardTagList = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CardImage> cardImageList = new ArrayList<>();

    @OneToMany(mappedBy = "cardMemo")
    private List<CardMemo> cardMomoList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "copy_info_id")
    private CopyInfo copyInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;

    @Builder
    public Card(String title, String content, LocalDateTime accessTime, CardType cardType, Long userId) {
        this.title = title;
        this.content = content;
        this.accessTime = accessTime;
        this.cardType = cardType;
        this.userId = userId;
    }

    public static Card creatEmptyCard(String type, Long userId) {
        return Card.builder()
            .title("")
            .content("")
            .userId(userId)
            .cardType(CardType.findByValue(type))
            .accessTime(LocalDateTime.now())
            .build();
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }


}
