package com.server.bbo_gak.domain.card.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippet;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.server.bbo_gak.domain.card.dao.CardTagRepository;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Sql({"/all-data-delete.sql", "/card-test-data.sql"})
public class TagControllerTest extends AbstractRestDocsTests {

    @Autowired
    private CardTagRepository cardTagRepository;

    private static final String DEFAULT_URL = "/api/v1";

    @Nested
    class 전체_태그_목록_조회 {

        @Test
        @Transactional
        public void 성공() throws Exception {

            // TEST
            ResultActions resultActions = mockMvc.perform(getRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

            // DOCS
            resultActions.andDo(document("[태그_전체_목록_조회] 성공", resource(getSuccessResponseResource())));
        }

        private MockHttpServletRequestBuilder getRequest() {
            return get(DEFAULT_URL + "/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        }

        private ResourceSnippetParameters getSuccessResponseResource() {
            return ResourceSnippetParameters.builder()
                .description("전체 카드 태그 목록").tags("Tag")
                .responseSchema(Schema.schema("TagGetResponse"))
                .responseFields(
                    fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("태그 ID"),
                    fieldWithPath("[].name").type(JsonFieldType.STRING).description("태그 이름"),
                    fieldWithPath("[].type").type(JsonFieldType.STRING).description("태그 타입"))
                .build();
        }
    }

    @Nested
    class 전체_태그_목록_조회_공고에서 {

        @Test
        @Transactional
        public void 성공() throws Exception {

            // TEST
            ResultActions resultActions = mockMvc.perform(getRequest())
                .andExpect(status().isOk());

            // DOCS
            resultActions.andDo(document("[전체_태그_목록_조회_공고에서] 성공", resource(getSuccessResponseResource())));
        }

        private MockHttpServletRequestBuilder getRequest() {
            return get(DEFAULT_URL + "/recruits/{recruit-id}/tags", 1L)
                .queryParam("type", "인터뷰_준비")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        }

        private ResourceSnippetParameters getSuccessResponseResource() {
            return ResourceSnippetParameters.builder()
                .description("전체 카드 태그 목록").tags("Tag")
                .pathParameters(
                    parameterWithName("recruit-id").description("recruit-id")
                )
                .queryParameters(
                    parameterWithName("type").description("type")
                )
                .responseSchema(Schema.schema("TagGetResponse"))
                .responseFields(
                    fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("태그 ID"),
                    fieldWithPath("[].name").type(JsonFieldType.STRING).description("태그 이름"),
                    fieldWithPath("[].type").type(JsonFieldType.STRING).description("태그 타입"))
                .build();
        }
    }


    @Nested
    class 카드_태그_목록_조회 {

        @Test
        @Transactional
        public void 성공() throws Exception {

            // TEST
            Long givenCardId = 1L;

            ResultActions resultActions = mockMvc.perform(getRequest(givenCardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[?(@.id == 1)]").doesNotExist());

            // DOCS
            resultActions.andDo(document("[카드_태그_목록_조회] 성공", resource(getSuccessResponseResource())));
        }

        @Test
        @Transactional
        public void 카드_찾기_실패() throws Exception {

            // TEST
            Long givenCardId = 9999L;

            ResultActions resultActions = mockMvc.perform(getRequest(givenCardId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(ErrorCode.CARD_NOT_FOUND.getMessage()));

            // DOCS
            resultActions.andDo(document("[카드_태그_목록_조회] 카드 찾기 실패", resource(getErrorResponseResource())));
        }

        private MockHttpServletRequestBuilder getRequest(Long givenCardId) {
            return get(DEFAULT_URL + "/cards/{card-id}/tags", givenCardId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        }

        private ResourceSnippetParameters getSuccessResponseResource() {
            return ResourceSnippetParameters.builder()
                .description("카드 태그 목록").tags("Tag")
                .pathParameters(
                    parameterWithName("card-id").description("Card id")
                )
                .responseSchema(Schema.schema("TagGetResponse"))
                .responseFields(
                    fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("태그 ID"),
                    fieldWithPath("[].name").type(JsonFieldType.STRING).description("태그 이름"),
                    fieldWithPath("[].type").type(JsonFieldType.STRING).description("태그 타입"))
                .build();
        }

        private ResourceSnippetParameters getErrorResponseResource() {
            return ResourceSnippetParameters.builder()
                .description("카드 태그 목록").tags("Tag")
                .pathParameters(
                    parameterWithName("card-id").description("Card id")
                )
                .responseSchema(Schema.schema("ErrorResponse"))
                .responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")
                )
                .build();
        }

    }

    @Nested
    class 카드_태그_추가 {

        @Test
        @Transactional
        public void 성공() throws Exception {

            // TEST
            Long givenCardId = 1L;
            Long givenTagId = 3L;

            ResultActions resultActions = mockMvc.perform(getRequest(givenCardId, givenTagId))
                .andExpect(status().isOk());

            assertTrue(isExistExpectedCardTag(givenCardId, givenTagId));

            // DOCS
            resultActions.andDo(document("[카드_태그_추가] 생성", resource(getSuccessResponseResource())));
        }

        @Test
        @Transactional
        public void 태그_중복_추가() throws Exception {

            // TEST
            Long givenCardId = 1L;
            Long givenTagId = 1L;

            ResultActions resultActions = mockMvc.perform(getRequest(givenCardId, givenTagId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(ErrorCode.TAG_DUPLICATED.getMessage()));

            // DOCS
            resultActions.andDo(document("[카드_태그_추가] 태그 중복 추가", resource(getErrorResponseResource())));
        }

        @Test
        @Transactional
        public void 태그_찾기_실패() throws Exception {

            // TEST
            Long givenCardId = 999L;
            Long givenTagId = 999L;

            ResultActions resultActions = mockMvc.perform(getRequest(givenCardId, givenTagId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(ErrorCode.TAG_NOT_FOUND.getMessage()));

            assertFalse(isExistExpectedCardTag(givenCardId, givenTagId));

            // DOCS
            resultActions.andDo(document("[카드_태그_추가] 태그 찾기 실패", resource(getErrorResponseResource())));
        }

        @Test
        @Transactional
        public void 카드_찾기_실패() throws Exception {

            // TEST
            Long givenCardId = 9999L;
            Long givenTagId = 1L;

            ResultActions resultActions = mockMvc.perform(getRequest(givenCardId, givenTagId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(ErrorCode.CARD_NOT_FOUND.getMessage()));

            assertFalse(isExistExpectedCardTag(givenCardId, givenTagId));

            // DOCS
            resultActions.andDo(document("[카드_찾기_실패] 카드 찾기 실패", resource(getErrorResponseResource())));
        }

        private MockHttpServletRequestBuilder getRequest(Long givenCardId, Long givenTagId) {
            return post(DEFAULT_URL + "/cards/{card-id}/tag/{tag-id}", givenCardId, givenTagId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        }

        private ResourceSnippetParameters getSuccessResponseResource() {
            return ResourceSnippetParameters.builder()
                .description("카드 태그 추가").tags("Tag")
                .pathParameters(
                    parameterWithName("card-id").description("Card id"),
                    parameterWithName("tag-id").description("Tag id")
                )
                .build();
        }

        private ResourceSnippetParameters getErrorResponseResource() {
            return ResourceSnippetParameters.builder()
                .description("카드 태그 추가").tags("Tag")
                .pathParameters(
                    parameterWithName("card-id").description("Card id"),
                    parameterWithName("tag-id").description("Tag id")
                )
                .responseSchema(Schema.schema("ErrorResponse"))
                .responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")
                )
                .build();
        }

        private boolean isExistExpectedCardTag(Long givenCardId, Long givenTagId) {
            return cardTagRepository.findAll().stream()
                .anyMatch(cardTag -> cardTag.getCard().getId().equals(givenCardId)
                    && cardTag.getTag().getId().equals(givenTagId));
        }
    }


    @Nested
    class 카드_태그_삭제 {

        @Test
        @Transactional
        public void 성공() throws Exception {

            // TEST
            Long givenCardId = 1L;
            Long givenTagId = 1L;

            ResultActions resultActions = mockMvc.perform(getRequest(givenCardId, givenTagId))
                .andExpect(status().isOk());

            assertFalse(cardTagRepository.findById(1L).isPresent());

            // DOCS
            resultActions.andDo(document("[카드_태그_삭제] 성공", getSuccessResponseResource()));
        }


        @Test
        @Transactional
        public void 카드_찾기_실패() throws Exception {

            // TEST
            Long givenCardId = 999L;
            Long givenTagId = 1L;

            ResultActions resultActions = mockMvc.perform(getRequest(givenCardId, givenTagId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(ErrorCode.CARD_NOT_FOUND.getMessage()));

            assertTrue(cardTagRepository.findById(1L).isPresent());

            // DOCS
            resultActions.andDo(document("[카드_태그_삭제] 카드 찾기 실패", resource(getErrorResponseResource())));
        }

        @Test
        @Transactional
        public void 태그_찾기_실패() throws Exception {

            // TEST
            Long givenCardId = 1L;
            Long givenTagId = 999L;

            ResultActions resultActions = mockMvc.perform(getRequest(givenCardId, givenTagId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(ErrorCode.TAG_NOT_FOUND.getMessage()));

            assertTrue(cardTagRepository.findById(1L).isPresent());

            // DOCS
            resultActions.andDo(document("[카드_태그_삭제] 태그 찾기 실패", resource(getErrorResponseResource())));
        }

        @Test
        @Transactional
        public void 카드_태그_매핑_찾기_실패() throws Exception {

            // TEST
            Long givenCardId = 1L;
            Long givenTagId = 3L;

            ResultActions resultActions = mockMvc.perform(getRequest(givenCardId, givenTagId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(ErrorCode.CARD_TAG_NOT_FOUND.getMessage()));

            assertTrue(cardTagRepository.findById(1L).isPresent());

            // DOCS
            resultActions.andDo(document("[카드_태그_삭제] 카드 태그 매핑 찾기 실패", resource(getErrorResponseResource())));

        }

        private MockHttpServletRequestBuilder getRequest(Long givenCardId, Long givenTagId) {
            return delete(DEFAULT_URL + "/cards/{card-id}/tag/{tag-id}", givenCardId, givenTagId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        }

        private ResourceSnippet getSuccessResponseResource() {
            return resource(
                ResourceSnippetParameters.builder()
                    .description("카드 태그 삭제").tags("Tag")
                    .pathParameters(
                        parameterWithName("card-id").description("Card id"),
                        parameterWithName("tag-id").description("Tag id")
                    )
                    .build()
            );
        }

        private ResourceSnippetParameters getErrorResponseResource() {
            return ResourceSnippetParameters.builder()
                .description("카드 태그 삭제").tags("Tag")
                .pathParameters(
                    parameterWithName("card-id").description("Card id"),
                    parameterWithName("tag-id").description("Tag id")
                )
                .responseSchema(Schema.schema("ErrorResponse"))
                .responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP status code")
                )
                .build();
        }
    }
}
