package com.server.bbo_gak.domain.card.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.server.bbo_gak.global.constant.FileExtension;
import org.junit.jupiter.api.Test;

public class CardImageTest {

    @Test
    public void of_정적_팩토리_메서드_테스트() {
        // Given
        Card card = new Card();
        setField(card, "id", 1L);

        String fileName = "images/card/12345678.png";

        // When
        CardImage cardImage = CardImage.of(card, fileName);

        // Then
        assertNotNull(cardImage);
        assertEquals("12345678", cardImage.getImageKey());
        assertEquals(FileExtension.PNG, cardImage.getImageFileExtension());
        assertEquals(fileName, cardImage.getFileName());
        assertEquals(card, cardImage.getCard());
    }
}
