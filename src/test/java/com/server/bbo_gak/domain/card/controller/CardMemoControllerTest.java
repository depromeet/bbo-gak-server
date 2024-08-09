package com.server.bbo_gak.domain.card.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.server.bbo_gak.domain.card.dao.CardMemoRepository;
import com.server.bbo_gak.domain.card.dto.request.CardMemoContentUpdateRequest;
import com.server.bbo_gak.domain.card.dto.request.CardMemoCreateRequest;
import com.server.bbo_gak.domain.card.dto.response.CardMemoCreateResponse;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.RestDocsFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Sql("/card-test-data.sql")
public class CardMemoControllerTest extends AbstractRestDocsTests {

    private static final String DEFAULT_URL = "/api/v1/cards";

    private final String cardMemoTag = "CardMemo";

    @Autowired
    private RestDocsFactory restDocsFactory;

    @Autowired
    private CardMemoRepository cardMemoRepository;

    @Nested
    @Transactional
    class 카드_메모_생성 {

        @Test
        public void 성공() throws Exception {

            CardMemoCreateRequest request = new CardMemoCreateRequest("test content");

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{card-id}/card-memo", request, HttpMethod.POST,
                        objectMapper, 1))
                .andExpect(status().isOk())
                .andDo(result -> restDocsFactory.getSuccessResource("[카드_메모_생성] 성공", "카드_메모_생성",
                        cardMemoTag, request,
                        objectMapper.readValue(result.getResponse().getContentAsString(), CardMemoCreateResponse.class))
                    .handle(result));

            assertTrue(cardMemoRepository.findById(1L).isPresent());
        }


        @Test
        public void 카드_찾기_실패() throws Exception {

            CardMemoCreateRequest request = new CardMemoCreateRequest("test content");

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{card-id}/card-memo", request, HttpMethod.POST,
                        objectMapper, 999))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[카드_메모_생성] 카드_찾기_실패", cardMemoTag, request));
        }
    }

    @Nested
    @Transactional
    class 카드_메모_조회 {

        @Test
        public void 성공() throws Exception {

            mockMvc.perform(getRequest(1L))
                .andExpect(status().isOk())
                .andDo(document("[카드_메모_조회] 성공", resource(getSuccessResponseResource())));
        }

        private MockHttpServletRequestBuilder getRequest(Long givenCardId) {
            return get(DEFAULT_URL + "/{card-id}/card-memo", givenCardId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        }

        private ResourceSnippetParameters getSuccessResponseResource() {
            return ResourceSnippetParameters.builder()
                .description("카드_메모_조회").tags(cardMemoTag)
                .responseSchema(Schema.schema(CardMemoCreateResponse.class.getName()))
                .pathParameters(parameterWithName("card-id").description("card-id"))
                .build();
        }
    }

    @Nested
    @Transactional
    class 카드_메모_내용_수정 {

        @Test
        public void 성공() throws Exception {

            String updatedContent = "updated contents";
            CardMemoContentUpdateRequest request = new CardMemoContentUpdateRequest(updatedContent);

            mockMvc.perform(getRequest(1L, request))
                .andExpect(status().isOk())
                .andDo(document("[카드_메모_내용_수정] 성공", resource(getSuccessResponseResource())));

            assertTrue(cardMemoRepository.findById(1L).get().getContent().equals(updatedContent));

        }

        private MockHttpServletRequestBuilder getRequest(Long givenCardMemoId, CardMemoContentUpdateRequest request)
            throws Exception {

            return put(DEFAULT_URL + "/{card-id}/card-memo/{card-memo-id}/content", 1L, givenCardMemoId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        }

        private ResourceSnippetParameters getSuccessResponseResource() {
            return ResourceSnippetParameters.builder()
                .description("카드_메모_조회").tags(cardMemoTag)
                .responseSchema(Schema.schema(CardMemoCreateResponse.class.getName()))
                .pathParameters(
                    parameterWithName("card-id").description("card-id"),
                    parameterWithName("card-memo-id").description("card-memo-id"))
                .build();
        }
    }

    @Nested
    @Transactional
    class 카드_메모_삭제 {

        @Test
        public void 성공() throws Exception {

            long givenCardMemoId = 1L;

            mockMvc.perform(getRequest(givenCardMemoId))
                .andExpect(status().isOk())
                .andDo(document("[카드_메모_삭제] 성공", resource(getSuccessResponseResource())));
        }

        private MockHttpServletRequestBuilder getRequest(Long givenCardMemoId) {
            return delete(DEFAULT_URL + "/{card-id}/card-memo/{card-memo-id}", 1L, givenCardMemoId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        }

        private ResourceSnippetParameters getSuccessResponseResource() {
            return ResourceSnippetParameters.builder()
                .description("카드_메모_삭제").tags(cardMemoTag)
                .pathParameters(
                    parameterWithName("card-id").description("card-id"),
                    parameterWithName("card-memo-id").description("card-memo-id"))
                .build();
        }
    }
}
