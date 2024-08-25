package com.server.bbo_gak.domain.card.entity;

import com.server.bbo_gak.global.common.BaseEntity;
import com.server.bbo_gak.global.constant.FileExtension;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE card_image SET deleted = true WHERE card_image_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardImage extends BaseEntity {

    private final String TYPE = "card_content";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_image_id")
    private Long id;
    @Column(length = 36)
    private String imageKey;

    @Enumerated(EnumType.STRING)
    private FileExtension imageFileExtension;

    private String fileName;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @Builder
    public CardImage(String imageKey, FileExtension imageFileExtension, String fileName, String imageUrl,
        Card card) {
        this.imageKey = imageKey;
        this.imageFileExtension = imageFileExtension;
        this.fileName = fileName;
        this.imageUrl = imageUrl;
        this.card = card;
    }

    public static CardImage of(Card card, String fileName) {
        String imageKey = extractImageKey(fileName);
        FileExtension fileExtension = extractFileExtension(fileName);
        return CardImage.builder()
            .card(card)
            .imageKey(imageKey)
            .imageFileExtension(fileExtension)
            .fileName(fileName)
            .build();
    }

    private static String extractImageKey(String fileName) {
        return fileName.split("/")[2].split("\\.")[0];
    }

    private static FileExtension extractFileExtension(String fileName) {
        return FileExtension.from(fileName.split("/")[2].split("\\.")[1]);
    }
}
