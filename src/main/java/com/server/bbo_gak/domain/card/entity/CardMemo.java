package com.server.bbo_gak.domain.card.entity;

import com.server.bbo_gak.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE card_memo SET deleted = true WHERE card_memo_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardMemo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_memo_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    public CardMemo(Card card, String content) {
        this.content = content;
        this.card = card;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
