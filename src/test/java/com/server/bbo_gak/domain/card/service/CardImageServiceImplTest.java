package com.server.bbo_gak.domain.card.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.server.bbo_gak.domain.card.dao.CardImageRepository;
import com.server.bbo_gak.domain.card.dao.CardRepository;
import com.server.bbo_gak.domain.card.dto.request.CardImageDeleteRequest;
import com.server.bbo_gak.domain.card.dto.request.CardImageUploadCompleteRequest;
import com.server.bbo_gak.domain.card.dto.response.CardImageUploadCompleteResponse;
import com.server.bbo_gak.domain.card.entity.Card;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import com.server.bbo_gak.global.utils.s3.S3Util;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CardImageServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardImageRepository cardImageRepository;

    @Mock
    private S3Util s3Util;

    @InjectMocks
    private CardImageServiceImpl cardImageService;

    private Card card;
    private List<CardImageUploadCompleteRequest> uploadValidRequest;
    private CardImageDeleteRequest deleteRequest;


    @BeforeEach
    void setUp() {
        card = Card.builder().build();
        setField(card, "id", 1L);

        uploadValidRequest = Arrays.asList(
            new CardImageUploadCompleteRequest("card/1/file1.png"),
            new CardImageUploadCompleteRequest("card/1/file2.png"));
        deleteRequest = new CardImageDeleteRequest(1L, "file1.png");
    }

    @Nested
    class 이미지_삭제 {

        @Test
        void 이미지_삭제_성공() {
            when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
            doNothing().when(cardImageRepository).deleteByCardAndImageUrl(any(Card.class), anyString());

            cardImageService.deleteCardImage(deleteRequest);

            verify(cardImageRepository, times(1)).deleteByCardAndImageUrl(any(Card.class), anyString());
        }

        @Test
        void 이미지_삭제_카드_못찾음() {
            when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> cardImageService.deleteCardImage(deleteRequest));
        }
    }


    @Nested
    class 이미지_업로드 {

        @Test
        void 카드에_이미지_추가_성공() {

            when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
            when(s3Util.getS3ObjectUrl(anyString())).thenAnswer(
                invocation -> "https://example.com/" + invocation.getArgument(0));

            List<CardImageUploadCompleteResponse> responses = cardImageService.addImagesToCard(1L,
                uploadValidRequest);

            assertEquals(2, responses.size());
            assertEquals("https://example.com/card/1/file1.png", responses.get(0).staticUrl());
            assertEquals("https://example.com/card/1/file2.png", responses.get(1).staticUrl());

            verify(cardImageRepository).saveAll(anyList());
        }

        @Test
        void 카드에_이미지_추가_카드없음_실패() {
            when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> cardImageService.addImagesToCard(1L, uploadValidRequest));
        }
    }
}
