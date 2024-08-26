package com.server.bbo_gak.domain.card.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.server.bbo_gak.domain.card.dao.CardCopyInfoRepository;
import com.server.bbo_gak.domain.card.dao.CardRepository;
import com.server.bbo_gak.domain.card.dto.response.CardCreateResponse;
import com.server.bbo_gak.domain.card.dto.response.CardTypeCountInRecruitGetResponse;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.RestDocsFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Sql({"/all-data-delete.sql", "/card-test-data.sql"})
public class CardInRecruitControllerTest extends AbstractRestDocsTests {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardCopyInfoRepository cardCopyInfoRepository;

    @Autowired
    private RestDocsFactory restDocsFactory;

    private static final String DEFAULT_URL = "/api/v1";

    private final String cardInRecruit = "CardInRecruit";


    @Nested
    class 카드_타입_카운트_조회_공고에서 {

        @Test
        public void 성공() throws Exception {

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/recruits/{recruit-id}/cards/type-count", null,
                        HttpMethod.GET, objectMapper, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("서류_준비").value(1))
                .andExpect(jsonPath("과제_준비").value(2))
                .andExpect(jsonPath("인터뷰_준비").value(3))
                .andDo(
                    result -> restDocsFactory.getSuccessResource("[카드_타입_카운트_조회_공고에서] 성공", "카드_타입_카운트_조회_공고에서",
                            cardInRecruit,
                            null, objectMapper.readValue(result.getResponse().getContentAsString(),
                                CardTypeCountInRecruitGetResponse.class))
                        .handle(result));
        }
    }

    @Nested
    class 카드_리스트_조회_공고에서 {

        @Test
        public void 성공_태그_필터_없는_케이스() throws Exception {

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/recruits/{recruit-id}/cards", null,
                            HttpMethod.GET, objectMapper, 1L)
                        .queryParam("type", "인터뷰_준비")
                        .queryParam("tag-ids", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(document("[카드_리스트_조회_공고에서] 성공", preprocessResponse(prettyPrint()), resource(getBuild())));
        }

        @Test
        public void 성공_태그_필터_존재_하는_케이스() throws Exception {

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/recruits/{recruit-id}/cards", null,
                            HttpMethod.GET, objectMapper, 1L)
                        .queryParam("type", "인터뷰_준비"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(document("[카드_리스트_조회_공고에서] 성공", preprocessResponse(prettyPrint()), resource(getBuild())));
        }

        private ResourceSnippetParameters getBuild() {
            return ResourceSnippetParameters.builder().description("카드 리스트 조회").tags(cardInRecruit)
                .queryParameters(
                    parameterWithName("type").description("타입"),
                    parameterWithName("tag-ids").description("태그 아이디 리스트").optional())
                .responseSchema(Schema.schema("CardListGetResponse"))
                .responseFields(fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("Card ID"),
                    fieldWithPath("[].title").type(JsonFieldType.STRING).description("Card 제목"),
                    fieldWithPath("[].updatedDate").type(JsonFieldType.STRING).description("Card 수정일시"),
                    fieldWithPath("[].tagList").type(JsonFieldType.ARRAY).description("태그 리스트").optional(),
                    fieldWithPath("[].tagList.[].id").type(JsonFieldType.NUMBER).description("태그 ID"),
                    fieldWithPath("[].tagList.[].name").type(JsonFieldType.STRING).description("태그 이름"),
                    fieldWithPath("[].tagList.[].type").type(JsonFieldType.STRING).description("태그 타입"))
                .build();
        }
    }

    @Nested
    class 카드_공고로_복사 {

        @Test
        @Transactional
        public void 성공() throws Exception {

            Long originalCardId = 1L;

            ResultActions resultActions = mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/recruits/{recruit-id}/cards/{card-id}", null,
                        HttpMethod.POST, objectMapper, 1L, originalCardId))
                .andExpect(status().isOk());

            resultActions.andDo(
                result ->
                    restDocsFactory.getSuccessResource("[카드_공고로_복사] 성공", "카드_공고로_복사", cardInRecruit,
                            null,
                            objectMapper.readValue(result.getResponse().getContentAsString(), CardCreateResponse.class))
                        .handle(result));


        }
    }
}
