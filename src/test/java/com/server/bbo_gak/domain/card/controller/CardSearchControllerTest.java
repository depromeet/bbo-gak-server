package com.server.bbo_gak.domain.card.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Sql({"/all-data-delete.sql", "/card-test-data.sql"})
public class CardSearchControllerTest extends AbstractRestDocsTests {

    private static final String DEFAULT_URL = "/api/v1";

    private final String cardSearch = "CardSearch";

    @Nested
    class 카드_검색_태그_리스트로 {

        @Test
        public void 성공() throws Exception {

            mockMvc.perform(get(DEFAULT_URL + "/search/cards").contentType(MediaType.APPLICATION_JSON)
                    .queryParam("tag-ids", "1,2")
                    .queryParam("card-type-value-group", "내_정보")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("[카드_검색_태그_리스트로] 성공", preprocessResponse(prettyPrint()), resource(getBuild())));
        }

        private ResourceSnippetParameters getBuild() {
            return ResourceSnippetParameters.builder().description("카드_검색_태그_리스트로").tags(cardSearch)
                .queryParameters(
                    parameterWithName("tag-ids").description("태그 아이디 리스트"),
                    parameterWithName("card-type-value-group").description("카드 타입 그룹").optional())
                .responseSchema(Schema.schema("CardListGetResponse"))
                .responseFields(fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("Card ID"),
                    fieldWithPath("[].title").type(JsonFieldType.STRING).description("Card 제목"),
                    fieldWithPath("[].updatedDate").type(JsonFieldType.STRING).description("Card 수정일시"),
                    fieldWithPath("[].tagList.[].id").type(JsonFieldType.NUMBER).description("태그 ID"),
                    fieldWithPath("[].tagList.[].name").type(JsonFieldType.STRING).description("태그 이름"),
                    fieldWithPath("[].tagList.[].type").type(JsonFieldType.STRING).description("태그 타입"),
                    fieldWithPath("[].cardTypeValueGroup").type(JsonFieldType.STRING).description("Card 타입 그룹"),
                    fieldWithPath("[].cardTypeValue").type(JsonFieldType.STRING).description("Card 타입"),
                    fieldWithPath("[].recruitTitle").type(JsonFieldType.STRING).description("Card 공고 제목").optional(),
                    fieldWithPath("[].content").type(JsonFieldType.STRING).description("Card 본문"))
                .build();
        }
    }

    @Nested
    class 카드_태그_검색_히스토리_조회 {

        @Test
        @Transactional
        public void 성공() throws Exception {

            mockMvc.perform(get(DEFAULT_URL + "/search/card-tag-history").contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("[카드_태그_검색_히스토리_조회] 성공", preprocessResponse(prettyPrint()), resource(getBuild())));
        }

        private ResourceSnippetParameters getBuild() {
            return ResourceSnippetParameters.builder()
                .description("카드_태그_검색_히스토리_조회").tags(cardSearch)
                .responseSchema(Schema.schema("TagGetResponse"))
                .responseFields(
                    fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("태그 ID"),
                    fieldWithPath("[].name").type(JsonFieldType.STRING).description("태그 이름"),
                    fieldWithPath("[].type").type(JsonFieldType.STRING).description("태그 타입"))
                .build();
        }
    }
}
