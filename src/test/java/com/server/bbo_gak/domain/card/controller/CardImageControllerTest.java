package com.server.bbo_gak.domain.card.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.server.bbo_gak.domain.card.dto.request.CardImageDeleteRequest;
import com.server.bbo_gak.domain.card.dto.request.CardImageUploadCompleteRequest;
import com.server.bbo_gak.domain.card.dto.response.CardImageUploadCompleteResponse;
import com.server.bbo_gak.domain.card.service.CardImageService;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@ActiveProfiles("test")
public class CardImageControllerTest extends AbstractRestDocsTests {

    private static final String DEFAULT_URL = "/api/v1/card-images";

    @MockBean
    private CardImageService cardImageService;

    private CardImageUploadCompleteRequest uploadRequest;
    private CardImageDeleteRequest deleteRequest;
    private List<CardImageUploadCompleteResponse> uploadResponses;

    @BeforeEach
    void setUp() {
        uploadRequest = new CardImageUploadCompleteRequest(1L, Arrays.asList("file1.png", "file2.png"));

        uploadResponses = Arrays.asList(
            new CardImageUploadCompleteResponse("https://example.com/file1.png"),
            new CardImageUploadCompleteResponse("https://example.com/file2.png")
        );
    }


    @Nested
    class 카드이미지_업로드_완료 {

        @Test
        public void 성공() throws Exception {
            when(cardImageService.addImagesToCard(any(CardImageUploadCompleteRequest.class))).thenReturn(
                uploadResponses);

            ResultActions resultActions = mockMvc.perform(
                    post(DEFAULT_URL + "/static-urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(uploadRequest))
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(
                    MockMvcRestDocumentationWrapper.document("[upload] 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 이미지 압로드 완료")
                                .tags("CardImage")
                                .requestSchema(Schema.schema("CardImageUploadCompleteRequest"))
                                .responseSchema(Schema.schema("Void"))
                                .requestFields(
                                    fieldWithPath("cardId").type(JsonFieldType.NUMBER).description("카드 id"),
                                    fieldWithPath("fileNames").type(JsonFieldType.ARRAY).description("이미지 파일이름 배열")
                                )
                                .responseFields(
                                    fieldWithPath("[].staticUrl").type(JsonFieldType.STRING)
                                        .description("S3에 업로드된 이미지 Url")
                                )
                                .build()
                        )
                    )
                );
        }

        @Test
        public void 카드_찾기_실패() throws Exception {
            CardImageUploadCompleteRequest invalidRequest = new CardImageUploadCompleteRequest(0L,
                List.of("fileName1", "fileName2"));

            when(cardImageService.addImagesToCard(any(CardImageUploadCompleteRequest.class))).thenThrow(
                new BusinessException(ErrorCode.CARD_NOT_FOUND));

            mockMvc.perform(
                    post(DEFAULT_URL + "/static-urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andDo(
                    MockMvcRestDocumentationWrapper.document("[upload] 카드 찾기 실패",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 이미지 압로드 완료")
                                .tags("CardImage")
                                .requestSchema(Schema.schema("CardImageUploadCompleteRequest"))
                                .responseSchema(Schema.schema("ErrorResponse"))
                                .requestFields(
                                    fieldWithPath("cardId").type(JsonFieldType.NUMBER).description("카드 id"),
                                    fieldWithPath("fileNames").type(JsonFieldType.ARRAY).description("이미지 파일이름 배열")
                                )
                                .responseFields(
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")
                                )
                                .build()
                        )
                    )
                );
        }

        @Test
        public void S3에서_업로드된_이미지_찾기_실패() throws Exception {
            CardImageUploadCompleteRequest invalidRequest = new CardImageUploadCompleteRequest(2L,
                List.of("invalid-filename", "invalid-filename"));

            when(cardImageService.addImagesToCard(any(CardImageUploadCompleteRequest.class))).thenThrow(
                new BusinessException(ErrorCode.IMAGE_FILE_NOT_FOUND_IN_S3));

            mockMvc.perform(
                    post(DEFAULT_URL + "/static-urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andDo(
                    MockMvcRestDocumentationWrapper.document("[upload] S3에서_업로드된_이미지_찾기_실패",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 이미지 압로드 완료")
                                .tags("CardImage")
                                .requestSchema(Schema.schema("CardImageUploadCompleteRequest"))
                                .responseSchema(Schema.schema("ErrorResponse"))
                                .requestFields(
                                    fieldWithPath("cardId").type(JsonFieldType.NUMBER).description("카드 id"),
                                    fieldWithPath("fileNames").type(JsonFieldType.ARRAY).description("이미지 파일이름 배열")
                                )
                                .responseFields(
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")
                                )
                                .build()
                        )
                    )
                );
        }
    }

    @Nested
    class 카드이미지_삭제 {

        @Test
        public void 성공() throws Exception {
            doNothing().when(cardImageService).deleteCardImage(any(CardImageDeleteRequest.class));
            CardImageDeleteRequest request = new CardImageDeleteRequest(1L,
                "https://example.com/file2.png");

            ResultActions resultActions = mockMvc.perform(
                    delete(DEFAULT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNoContent())
                .andDo(
                    MockMvcRestDocumentationWrapper.document("[delete] 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 이미지 삭제")
                                .tags("CardImage")
                                .requestSchema(Schema.schema("CardImageDeleteRequest"))
                                .responseSchema(Schema.schema("Void"))
                                .requestFields(
                                    fieldWithPath("cardId").type(JsonFieldType.NUMBER)
                                        .description("카드 id"),
                                    fieldWithPath("staticUrl").type(JsonFieldType.STRING)
                                        .description("삭제할 카드 이미지의 staticUrl")
                                )
//                                .responseFields(
//                                    fieldWithPath("[].staticUrl").type(JsonFieldType.STRING)
//                                        .description("S3에 업로드된 이미지 Url")
//                                )
                                .build()
                        )
                    )
                );
        }

        @Test
        public void 카드_찾기_실패() throws Exception {

            CardImageDeleteRequest invalidRequest = new CardImageDeleteRequest(0L, "invalid-staticUrl");
            doThrow(new BusinessException(ErrorCode.CARD_NOT_FOUND)).when(cardImageService)
                .deleteCardImage(any(CardImageDeleteRequest.class));

            ResultActions resultActions = mockMvc.perform(
                    delete(DEFAULT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andDo(
                    MockMvcRestDocumentationWrapper.document("[delete] 카드 찾기 실패",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 이미지 삭제")
                                .tags("CardImage")
                                .requestSchema(Schema.schema("CardImageDeleteRequest"))
                                .responseSchema(Schema.schema("ErrorResponse"))
                                .requestFields(
                                    fieldWithPath("cardId").type(JsonFieldType.NUMBER)
                                        .description("카드 id"),
                                    fieldWithPath("staticUrl").type(JsonFieldType.STRING)
                                        .description("삭제할 카드 이미지의 staticUrl")
                                )
                                .responseFields(
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")
                                )
                                .build()
                        )
                    )
                );
        }

        @Test
        public void s3에서_삭제할_이미지_찾기_실패() throws Exception {
            CardImageDeleteRequest invalidRequest = new CardImageDeleteRequest(0L, "invalid-staticUrl");

            doThrow(new BusinessException(ErrorCode.CARD_NOT_FOUND)).when(cardImageService)
                .deleteCardImage(any(CardImageDeleteRequest.class));

            ResultActions resultActions = mockMvc.perform(
                    delete(DEFAULT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andDo(
                    MockMvcRestDocumentationWrapper.document("[delete] s3에서_삭제할_이미지_찾기_실패",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 이미지 삭제")
                                .tags("CardImage")
                                .requestSchema(Schema.schema("CardImageDeleteRequest"))
                                .responseSchema(Schema.schema("ErrorResponse"))
                                .requestFields(
                                    fieldWithPath("cardId").type(JsonFieldType.NUMBER)
                                        .description("카드 id"),
                                    fieldWithPath("staticUrl").type(JsonFieldType.STRING)
                                        .description("삭제할 카드 이미지의 staticUrl")
                                )
                                .responseFields(
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")
                                )
                                .build()
                        )
                    )
                );
        }
    }
}
