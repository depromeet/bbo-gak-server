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
import com.server.bbo_gak.domain.card.dto.request.CardCreateRequest;
import com.server.bbo_gak.domain.card.dto.request.CardTitleUpdateRequest;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import java.util.Arrays;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Sql("/card-test-data.sql")
public class CardControllerTest extends AbstractRestDocsTests {

    private static final String DEFAULT_URL = "/api/v1";
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CardTagRepository cardTagRepository;

    @Nested
    class 카드_타입_카운트_조회 {

        @Test
        public void 성공() throws Exception {

            mockMvc.perform(get(DEFAULT_URL + "/cards/type-count").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(
                document("[select] 카드 타입 카운트 조회", preprocessResponse(prettyPrint()), resource(
                    ResourceSnippetParameters.builder().description("카드 타입 카운트 조회").tags("Card")
                        .responseSchema(Schema.schema("CardTypeCountGetResponse"))
                        .responseFields(fieldWithPath("경험_정리").type(JsonFieldType.NUMBER).description("면접_질문_개수"),
                            fieldWithPath("자기소개서").type(JsonFieldType.NUMBER).description("자기소개서_개수"),
                            fieldWithPath("면접_질문").type(JsonFieldType.NUMBER).description("면접_질문_개수"))
                        .build())));
        }
    }

    @Nested
    class 카드_단건_조회 {

        @Test
        public void 성공() throws Exception {
            mockMvc.perform(get(DEFAULT_URL + "/cards/{card-id}", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(
                document("[select] 카드 단건 조회", preprocessResponse(prettyPrint()), resource(
                    ResourceSnippetParameters.builder().description("카드 단건 조회").tags("Card")
                        .pathParameters(parameterWithName("card-id").description("card-id"))
                        .responseSchema(Schema.schema("CardGetResponse"))
                        .responseFields(fieldWithPath("title").type(JsonFieldType.STRING).description("Card 제목"),
                            fieldWithPath("content").type(JsonFieldType.STRING).description("Card 내용"),
                            fieldWithPath("cardTypeValueList").type(JsonFieldType.ARRAY).description("Card 타입값 리스트"),
                            fieldWithPath("updatedDate").type(JsonFieldType.STRING).description("Card 수정일시"),
                            fieldWithPath("tagList.[].id").type(JsonFieldType.NUMBER).description("태그 ID"),
                            fieldWithPath("tagList.[].name").type(JsonFieldType.STRING).description("태그 이름"),
                            fieldWithPath("tagList.[].type").type(JsonFieldType.STRING).description("태그 타입"))
                        .build())));
        }

        @Test
        public void 카드_찾기_실패() throws Exception {
            mockMvc.perform(get(DEFAULT_URL + "/cards/{card-id}", 9999).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(
                document("[select] 카드 찾기 실패", preprocessResponse(prettyPrint()), resource(
                    ResourceSnippetParameters.builder().description("카드 단건 조회").tags("Card")
                        .pathParameters(parameterWithName("card-id").description("card-id"))
                        .responseSchema(Schema.schema("ErrorResponse")).responseFields(
                            fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                            fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")).build())));

        }
    }

    @Nested
    class 카드_리스트_조회 {

        @Test
        public void 성공() throws Exception {

            mockMvc.perform(
                get(DEFAULT_URL + "/cards").contentType(MediaType.APPLICATION_JSON).queryParam("type", "자기소개서")
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(
                document("[select] 카드 리스트 조회", preprocessResponse(prettyPrint()), resource(
                    ResourceSnippetParameters.builder().description("카드 리스트 조회").tags("Card")
                        .queryParameters(parameterWithName("type").description("타입"))
                        .responseSchema(Schema.schema("CardListGetResponse"))
                        .responseFields(fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("Card ID"),
                            fieldWithPath("[].title").type(JsonFieldType.STRING).description("Card 제목"),
                            fieldWithPath("[].updatedDate").type(JsonFieldType.STRING).description("Card 수정일시"),
                            fieldWithPath("[].tagList.[].id").type(JsonFieldType.NUMBER).description("태그 ID"),
                            fieldWithPath("[].tagList.[].name").type(JsonFieldType.STRING).description("태그 이름"),
                            fieldWithPath("[].tagList.[].type").type(JsonFieldType.STRING).description("태그 타입"))
                        .build())));
        }
    }


    @Nested
    class 카드_신규_생성 {

        CardCreateRequest request = new CardCreateRequest(Arrays.asList("경험_정리", "자기소개서"), Arrays.asList(1L, 2L));

        @Test
        @Transactional
        public void 성공() throws Exception {

            mockMvc.perform(post(DEFAULT_URL + "/card").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(document("[create] 카드 신규 생성", preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder().description("카드 신규 생성").tags("Card")
                        .requestSchema(Schema.schema("CardCreateRequest"))
                        .requestFields(fieldWithPath("cardTypeValueList").type(JsonFieldType.ARRAY).description("카드 타입값"),
                            fieldWithPath("tagIdList").type(JsonFieldType.ARRAY).description("태그 ID"))
                        .responseSchema(Schema.schema("CardCreateResponse"))
                        .responseFields(fieldWithPath("cardId").type(JsonFieldType.NUMBER).description("Card ID"))
                        .build())));
        }
    }

    @Nested
    class 카드_제목_수정 {

        @Test
        @Transactional
        public void 성공() throws Exception {

            String newTitle = "test title";
            CardTitleUpdateRequest request = new CardTitleUpdateRequest(newTitle);

            mockMvc.perform(put(DEFAULT_URL + "/cards/{card-id}/title", 1).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(document("[update] 카드 제목 수정", preprocessRequest(prettyPrint()),
                    resource(ResourceSnippetParameters.builder().description("카드 제목 수정").tags("Card")
                        .pathParameters(parameterWithName("card-id").description("Card id"))
                        .requestSchema(Schema.schema("CardTitleUpdateRequest"))
                        .requestFields(fieldWithPath("title").type(JsonFieldType.STRING).description("카드 제목")).build())));

            assertEquals(cardRepository.findById(1L).get().getTitle(), newTitle);
        }

        @Test
        @Transactional
        public void 카드_찾기_실패() throws Exception {

            String newTitle = "test title";
            CardTitleUpdateRequest request = new CardTitleUpdateRequest(newTitle);

            mockMvc.perform(put(DEFAULT_URL + "/cards/{card-id}/title", 999).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(document("[update] 카드 찾기 실패", preprocessRequest(prettyPrint()),
                    resource(ResourceSnippetParameters.builder().description("카드 제목 수정").tags("Card")
                        .pathParameters(parameterWithName("card-id").description("Card id"))
                        .responseSchema(Schema.schema("ErrorResponse"))
                        .responseFields(fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                            fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")).build())));

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

            mockMvc.perform(put(DEFAULT_URL + "/cards/{card-id}/content", 1).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(document("[카드 본문 수정] 성공", preprocessRequest(prettyPrint()), resource(
                    ResourceSnippetParameters.builder().description("카드 본문 수정").tags("Card")
                        .pathParameters(parameterWithName("card-id").description("Card id"))
                        .requestSchema(Schema.schema("CardContentUpdateRequest"))
                        .requestFields(fieldWithPath("content").type(JsonFieldType.STRING).description("카드 본문")).build())));

            assertEquals(cardRepository.findById(1L).get().getContent(), newContent);
        }

        @Test
        @Transactional
        public void 카드_찾기_실패() throws Exception {

            String newContent = "test content";
            CardContentUpdateRequest request = new CardContentUpdateRequest(newContent);

            mockMvc.perform(put(DEFAULT_URL + "/cards/{card-id}/content", 999).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(
                    document("[카드 본문 수정] 카드 찾기 실패", preprocessRequest(prettyPrint()), resource(
                        ResourceSnippetParameters.builder().description("카드 본문 수정").tags("Card")
                            .pathParameters(parameterWithName("card-id").description("Card id"))
                            .responseSchema(Schema.schema("ErrorResponse")).responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")).build())));

            assertNotEquals(cardRepository.findById(1L).get().getContent(), newContent);
        }
    }

    @Nested
    class 카드_삭제 {

        @Test
        @Transactional
        public void 성공() throws Exception {

            mockMvc.perform(delete(DEFAULT_URL + "/cards/{card-id}", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(document("[카드_삭제] 성공", resource(
                ResourceSnippetParameters.builder().description("카드 삭제").tags("Card")
                    .pathParameters(parameterWithName("card-id").description("Card id")).build())));

            assertFalse(cardRepository.findById(1L).isPresent());
            assertFalse(cardTagRepository.findById(1L).isPresent());
            assertFalse(cardTagRepository.findById(2L).isPresent());
        }

        @Test
        @Transactional
        public void 카드_찾기_실패() throws Exception {

            mockMvc.perform(delete(DEFAULT_URL + "/cards/{card-id}", 999).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(document("[카드_삭제] 카드 찾기 실패",
                resource(ResourceSnippetParameters.builder().description("카드 삭제").tags("Card")
                    .pathParameters(parameterWithName("card-id").description("Card id"))
                    .responseSchema(Schema.schema("ErrorResponse"))
                    .responseFields(fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                        fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")).build())));

            assertTrue(cardRepository.findById(1L).isPresent());
            assertTrue(cardTagRepository.findById(1L).isPresent());
            assertTrue(cardTagRepository.findById(2L).isPresent());
        }
    }
}
