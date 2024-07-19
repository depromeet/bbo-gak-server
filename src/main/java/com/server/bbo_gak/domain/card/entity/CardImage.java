package com.server.bbo_gak.domain.card.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardImage {

    private final String TYPE = "card_content";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_image_id")
    private Long id;
    @Column(length = 36)
    private String imageKey;

    @Enumerated(EnumType.STRING)
    private ImageFileExtension imageFileExtension;

    private String imageUrl;

    @Builder
    public CardImage(String imageKey, ImageFileExtension imageFileExtension, String imageUrl) {
        this.imageKey = imageKey;
        this.imageFileExtension = imageFileExtension;
        this.imageUrl = imageUrl;
    }
}
