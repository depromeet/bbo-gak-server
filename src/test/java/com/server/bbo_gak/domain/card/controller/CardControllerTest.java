package com.server.bbo_gak.domain.card.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.server.bbo_gak.domain.card.dao.CardRepository;
import com.server.bbo_gak.domain.card.dao.CardTagRepository;
import com.server.bbo_gak.domain.card.dto.request.CardContentUpdateRequest;
import com.server.bbo_gak.domain.card.dto.request.CardTitleUpdateRequest;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Sql("/card-test-data.sql")
public class CardControllerTest extends AbstractRestDocsTests {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardTagRepository cardTagRepository;

    private static final String DEFAULT_URL = "/api/v1";

    @BeforeEach
    void setUp() {

    }

    @Nested
    class 카드_단건_조회 {

        @Test
        public void 성공() throws Exception {
            mockMvc.perform(
                    get(DEFAULT_URL + "/cards/{card-id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("[select] 카드 단건 조회",
                        preprocessResponse(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 단건 조회").tags("Card")
                                .pathParameters(
                                    parameterWithName("card-id").description("card-id")
                                )
                                .responseSchema(Schema.schema("CardGetResponse"))
                                .responseFields(
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("Card 제목"),
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("Card 내용"),
                                    fieldWithPath("type").type(JsonFieldType.STRING).description("Card 타입"),
                                    fieldWithPath("updateDate").type(JsonFieldType.STRING).description("Card 수정일시"),
                                    fieldWithPath("cardTagList.[].id").type(JsonFieldType.NUMBER)
                                        .description("Card 태그 id"),
                                    fieldWithPath("cardTagList.[].name").type(JsonFieldType.STRING)
                                        .description("Card 태그 이름"),
                                    fieldWithPath("cardTagList.[].type").type(JsonFieldType.STRING)
                                        .description("Card 태그 타입"))
                                .build()
                        )
                    )
                );
        }

        @Test
        public void 카드_찾기_실패() throws Exception {
            mockMvc.perform(
                    get(DEFAULT_URL + "/cards/{card-id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("[select] 카드 찾기 실패",
                        preprocessResponse(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 단건 조회").tags("Card")
                                .pathParameters(
                                    parameterWithName("card-id").description("card-id")
                                )
                                .responseSchema(Schema.schema("ErrorResponse"))
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
    class 카드_리스트_조회 {

        @Test
        public void 성공() throws Exception {

            mockMvc.perform(
                    get(DEFAULT_URL + "/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("type", "경험_정리")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                    document("[select] 카드 리스트 조회",
                        preprocessResponse(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 리스트 조회").tags("Card")
                                .queryParameters(
                                    parameterWithName("type").description("타입")
                                )
                                .responseSchema(Schema.schema("CardGetsResponse"))
                                .responseFields(
                                    fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("Card ID"),
                                    fieldWithPath("[].title").type(JsonFieldType.STRING).description("Card 제목"),
                                    fieldWithPath("[].updateDate").type(JsonFieldType.STRING).description("Card 수정일시"),
                                    fieldWithPath("[].cardTagList.[].id").type(JsonFieldType.NUMBER)
                                        .description("Card 태그 id"),
                                    fieldWithPath("[].cardTagList.[].name").type(JsonFieldType.STRING)
                                        .description("Card 태그 이름"),
                                    fieldWithPath("[].cardTagList.[].type").type(JsonFieldType.STRING)
                                        .description("Card 태그 타입"))
                                .build()
                        )
                    )
                );
        }
    }

    @Nested
    class 카드_타입_카운트_조회 {

        @Test
        public void 성공() throws Exception {

            mockMvc.perform(
                    get(DEFAULT_URL + "/cards/type-count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                    document("[select] 카드 타입 카운트 조회",
                        preprocessResponse(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 타입 카운트 조회").tags("Card")
                                .responseSchema(Schema.schema("CardTypeCountGetResponse"))
                                .responseFields(
                                    fieldWithPath("경험_정리").type(JsonFieldType.NUMBER).description("면접_질문_개수"),
                                    fieldWithPath("자기소개서").type(JsonFieldType.NUMBER).description("자기소개서_개수"),
                                    fieldWithPath("면접_질문").type(JsonFieldType.NUMBER).description("면접_질문_개수"))
                                .build()
                        )
                    )
                );
        }
    }

    @Nested
    class 카드_신규_생성 {

        @Test
        @Transactional
        public void 성공() throws Exception {

            mockMvc.perform(
                    post(DEFAULT_URL + "/card")
                        .queryParam("type", "경험_정리")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(
                    document("[create] 카드 신규 생성",
                        preprocessResponse(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 신규 생성").tags("Card")
                                .queryParameters(
                                    parameterWithName("type").description("타입")
                                )
                                .responseSchema(Schema.schema("CardCreateResponse"))
                                .responseFields(
                                    fieldWithPath("cardId").type(JsonFieldType.NUMBER).description("Card ID"),
                                    fieldWithPath("type").type(JsonFieldType.STRING).description("Card 타입"))
                                .build()
                        )
                    )
                );
        }
    }

    @Nested
    class 카드_태그_추가 {

        @Test
        @Transactional
        public void 성공() throws Exception {

            mockMvc.perform(
                    post(DEFAULT_URL + "/cards/{card-id}/tag/{tag-id}", 1, 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(
                    document("[create] 카드 태그 생성",
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 태그 추가").tags("Card")
                                .pathParameters(
                                    parameterWithName("card-id").description("Card id"),
                                    parameterWithName("tag-id").description("Tag id")
                                )
                                .build()
                        )
                    )
                );
        }

        @Test
        @Transactional
        public void 태그_찾기_실패() throws Exception {

            mockMvc.perform(
                    post(DEFAULT_URL + "/cards/{card-id}/tag/{tag-id}", 999, 99999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(
                    document("[create] 태그 찾기 실패",
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 태그 추가").tags("Card")
                                .pathParameters(
                                    parameterWithName("card-id").description("Card id"),
                                    parameterWithName("tag-id").description("Tag id")
                                )
                                .responseSchema(Schema.schema("ErrorResponse"))
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
        @Transactional
        public void 카드_찾기_실패() throws Exception {

            mockMvc.perform(
                    post(DEFAULT_URL + "/cards/{card-id}/tag/{tag-id}", 9999, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(
                    document("[create] 카드 찾기 실패",
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 태그 추가").tags("Card")
                                .pathParameters(
                                    parameterWithName("card-id").description("Card id"),
                                    parameterWithName("tag-id").description("Tag id")
                                )
                                .responseSchema(Schema.schema("ErrorResponse"))
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
    class 카드_제목_수정 {

        @Test
        @Transactional
        public void 성공() throws Exception {

            String newTitle = "test title";
            CardTitleUpdateRequest request = new CardTitleUpdateRequest(newTitle);

            mockMvc.perform(
                    put(DEFAULT_URL + "/cards/{card-id}/title", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                    document("[update] 카드 제목 수정",
                        preprocessRequest(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 제목 수정").tags("Card")
                                .pathParameters(
                                    parameterWithName("card-id").description("Card id")
                                )
                                .requestSchema(Schema.schema("CardTitleUpdateRequest"))
                                .requestFields(
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("카드 제목")
                                )
                                .build()
                        )
                    )
                );

            assertEquals(cardRepository.findById(1L).get().getTitle(), newTitle);
        }

        @Test
        @Transactional
        public void 카드_찾기_실패() throws Exception {

            String newTitle = "test title";
            CardTitleUpdateRequest request = new CardTitleUpdateRequest(newTitle);

            mockMvc.perform(
                    put(DEFAULT_URL + "/cards/{card-id}/title", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(
                    document("[update] 카드 찾기 실패",
                        preprocessRequest(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 제목 수정").tags("Card")
                                .pathParameters(
                                    parameterWithName("card-id").description("Card id")
                                )
                                .responseSchema(Schema.schema("ErrorResponse"))
                                .responseFields(
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")
                                )
                                .build()
                        )
                    )
                );

            assertNotEquals(cardRepository.findById(1L).get().getTitle(), newTitle);
        }
    }

    @Nested
    class 카드_본문_수정 {

        @Test
        @Transactional
        public void 성공() throws Exception {

            String newContent = "test content";
            CardContentUpdateRequest request = new CardContentUpdateRequest(newContent);

            mockMvc.perform(
                    put(DEFAULT_URL + "/cards/{card-id}/content", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                    document("[카드 본문 수정] 성공",
                        preprocessRequest(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 본문 수정").tags("Card")
                                .pathParameters(
                                    parameterWithName("card-id").description("Card id")
                                )
                                .requestSchema(Schema.schema("CardContentUpdateRequest"))
                                .requestFields(
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("카드 본문")
                                )
                                .build()
                        )
                    )
                );

            assertEquals(cardRepository.findById(1L).get().getContent(), newContent);
        }

        @Test
        @Transactional
        public void 카드_찾기_실패() throws Exception {

            String newContent = "test content";
            CardContentUpdateRequest request = new CardContentUpdateRequest(newContent);

            mockMvc.perform(
                    put(DEFAULT_URL + "/cards/{card-id}/content", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(
                    document("[카드 본문 수정] 카드 찾기 실패",
                        preprocessRequest(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 본문 수정").tags("Card")
                                .pathParameters(
                                    parameterWithName("card-id").description("Card id")
                                )
                                .responseSchema(Schema.schema("ErrorResponse"))
                                .responseFields(
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")
                                )
                                .build()
                        )
                    )
                );

            assertNotEquals(cardRepository.findById(1L).get().getContent(), newContent);
        }
    }

    @Nested
    class 카드_삭제 {

        @Test
        @Transactional
        public void 성공() throws Exception {

            mockMvc.perform(
                    delete(DEFAULT_URL + "/cards/{card-id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                    document("[카드_삭제] 성공",
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 삭제").tags("Card")
                                .pathParameters(
                                    parameterWithName("card-id").description("Card id")
                                )
                                .build()
                        )
                    )
                );

            assertFalse(cardRepository.findById(1L).isPresent());
            assertFalse(cardTagRepository.findById(1L).isPresent());
            assertFalse(cardTagRepository.findById(2L).isPresent());
        }

        @Test
        @Transactional
        public void 카드_찾기_실패() throws Exception {

            mockMvc.perform(
                    delete(DEFAULT_URL + "/cards/{card-id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(
                    document("[카드_삭제] 카드 찾기 실패",
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 삭제").tags("Card")
                                .pathParameters(
                                    parameterWithName("card-id").description("Card id")
                                )
                                .responseSchema(Schema.schema("ErrorResponse"))
                                .responseFields(
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")
                                )
                                .build()
                        )
                    )
                );

            assertTrue(cardRepository.findById(1L).isPresent());
            assertTrue(cardTagRepository.findById(1L).isPresent());
            assertTrue(cardTagRepository.findById(2L).isPresent());
        }
    }

    @Nested
    class 카드_태그_삭제 {

        @Test
        @Transactional
        public void 성공() throws Exception {

            mockMvc.perform(
                    delete(DEFAULT_URL + "/cards/{card-id}/tag/{tag-id}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                    document("[카드_태그_삭제] 성공",
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("카드 태그 삭제").tags("Card")
                                .pathParameters(
                                    parameterWithName("card-id").description("Card id"),
                                    parameterWithName("tag-id").description("Tag id")
                                )
                                .build()
                        )
                    )
                );

            assertFalse(cardTagRepository.findById(1L).isPresent());

        }
    }

    @Test
    @Transactional
    public void 카드_찾기_실패() throws Exception {

        mockMvc.perform(
                delete(DEFAULT_URL + "/cards/{card-id}/tag/{tag-id}", 999, 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andDo(
                document("[카드_태그_삭제] 카드 찾기 실패",
                    resource(
                        ResourceSnippetParameters.builder()
                            .description("카드 태그 삭제").tags("Card")
                            .pathParameters(
                                parameterWithName("card-id").description("Card id"),
                                parameterWithName("tag-id").description("Tag id")
                            )
                            .responseSchema(Schema.schema("ErrorResponse"))
                            .responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")
                            )
                            .build()
                    )
                )
            );

        assertTrue(cardTagRepository.findById(1L).isPresent());

    }

    @Test
    @Transactional
    public void 태그_찾기_실패() throws Exception {

        mockMvc.perform(
                delete(DEFAULT_URL + "/cards/{card-id}/tag/{tag-id}", 1, 999)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andDo(
                document("[카드_태그_삭제] 태그 찾기 실패",
                    resource(
                        ResourceSnippetParameters.builder()
                            .description("카드 태그 삭제").tags("Card")
                            .pathParameters(
                                parameterWithName("card-id").description("Card id"),
                                parameterWithName("tag-id").description("Tag id")
                            )
                            .responseSchema(Schema.schema("ErrorResponse"))
                            .responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")
                            )
                            .build()
                    )
                )
            );

        assertTrue(cardTagRepository.findById(1L).isPresent());

    }
}
