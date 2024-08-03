package com.server.bbo_gak.domain.card.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.server.bbo_gak.domain.card.dto.request.CardImageDeleteRequest;
import com.server.bbo_gak.domain.card.dto.request.CardImageUploadCompleteRequest;
import com.server.bbo_gak.domain.card.dto.response.CardImageUploadCompleteResponse;
import com.server.bbo_gak.domain.card.service.CardImageService;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.RestDocsFactory;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CardImageControllerTest extends AbstractRestDocsTests {

    private static final String DEFAULT_URL = "/api/v1/card-images";

    @MockBean
    private CardImageService cardImageService;

    private List<CardImageUploadCompleteRequest> uploadRequest;
    private CardImageDeleteRequest deleteRequest;
    private List<CardImageUploadCompleteResponse> uploadResponses;

    @Autowired
    private RestDocsFactory restDocsFactory;

    @BeforeEach
    void setUp() {
        uploadRequest = Arrays.asList(
            new CardImageUploadCompleteRequest("file1.png"),
            new CardImageUploadCompleteRequest("file2.png")
        );

        uploadResponses = Arrays.asList(
            new CardImageUploadCompleteResponse("https://example.com/file1.png"),
            new CardImageUploadCompleteResponse("https://example.com/file2.png")
        );
    }


    @Nested
    class 카드이미지_업로드_완료 {

        @Test
        public void 성공() throws Exception {
            when(cardImageService.addImagesToCard(any(Long.class), any(List.class))).thenReturn(
                uploadResponses);

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/static-urls/{cardId}", uploadRequest, HttpMethod.POST,
                        objectMapper, 1))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResourceList("[upload] 성공", "카드 이미지 압로드 완료", "CardImage",
                    uploadRequest, uploadResponses));
        }

        @Test
        public void 카드_찾기_실패() throws Exception {
            List<CardImageUploadCompleteRequest> invalidRequest = List.of(
                new CardImageUploadCompleteRequest("invalid-filename"),
                new CardImageUploadCompleteRequest("invalid-filename")
            );

            when(cardImageService.addImagesToCard(any(Long.class), any(List.class))).thenThrow(
                new BusinessException(ErrorCode.CARD_NOT_FOUND));

            mockMvc.perform(restDocsFactory.createRequestList(
                    DEFAULT_URL + "/static-urls/{cardId}", invalidRequest, HttpMethod.POST, objectMapper, 1))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResourceList("[upload] 카드 찾기 실패", "CardImage", invalidRequest));
        }

        @Test
        public void S3에서_업로드된_이미지_찾기_실패() throws Exception {
            List<CardImageUploadCompleteRequest> invalidRequest = List.of(
                new CardImageUploadCompleteRequest("invalid-filename"),
                new CardImageUploadCompleteRequest("invalid-filename")
            );

            when(cardImageService.addImagesToCard(any(Long.class), any(List.class))).thenThrow(
                new BusinessException(ErrorCode.IMAGE_FILE_NOT_FOUND_IN_S3));

            mockMvc.perform(
                    restDocsFactory.createRequestList(
                        DEFAULT_URL + "/static-urls/{cardId}", invalidRequest, HttpMethod.POST, objectMapper, 1))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResourceList("[upload] S3에서_업로드된_이미지_찾기_실패", "CardImage",
                    invalidRequest));
        }
    }

    @Nested
    class 카드이미지_삭제 {

        @Test
        public void 성공() throws Exception {
            doNothing().when(cardImageService).deleteCardImage(any(CardImageDeleteRequest.class));
            CardImageDeleteRequest request = new CardImageDeleteRequest(1L,
                "https://example.com/file2.png");

            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL, request, HttpMethod.DELETE,
                    objectMapper))
                .andExpect(status().isNoContent())
                .andDo(restDocsFactory.getSuccessResource("[upload] 성공", "카드 이미지 삭제", "CardImage", request,
                    null));
        }

        @Test
        public void 카드_찾기_실패() throws Exception {

            CardImageDeleteRequest invalidRequest = new CardImageDeleteRequest(0L, "invalid-staticUrl");
            doThrow(new BusinessException(ErrorCode.CARD_NOT_FOUND)).when(cardImageService)
                .deleteCardImage(any(CardImageDeleteRequest.class));

            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL, invalidRequest, HttpMethod.DELETE,
                    objectMapper))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[delete] 카드 찾기 실패", "CardImage", invalidRequest));
        }

        @Test
        public void s3에서_삭제할_이미지_찾기_실패() throws Exception {
            CardImageDeleteRequest invalidRequest = new CardImageDeleteRequest(0L, "invalid-staticUrl");

            doThrow(new BusinessException(ErrorCode.CARD_NOT_FOUND)).when(cardImageService)
                .deleteCardImage(any(CardImageDeleteRequest.class));

            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL, invalidRequest, HttpMethod.DELETE,
                    objectMapper))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[delete] s3에서_삭제할_이미지_찾기_실패", "CardImage", invalidRequest));
        }
    }
}
