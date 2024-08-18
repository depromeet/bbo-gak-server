package com.server.bbo_gak.domain.card.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.server.bbo_gak.domain.card.dao.CardCopyInfoRepository;
import com.server.bbo_gak.domain.card.dao.CardRepository;
import com.server.bbo_gak.domain.card.dto.response.CardCreateResponse;
import com.server.bbo_gak.domain.card.dto.response.CardListGetResponse;
import com.server.bbo_gak.domain.card.dto.response.CardTypeCountInRecruitGetResponse;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.RestDocsFactory;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
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
        public void 성공() throws Exception {

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/recruits/{recruit-id}/cards?type=인터뷰_준비", null,
                        HttpMethod.GET, objectMapper, 1L))
                .andExpect(status().isOk())
                .andDo(
                    result ->
                        restDocsFactory.getSuccessResourceList("[카드_리스트_조회_공고에서] 성공", "카드_리스트_조회_공고에서", cardInRecruit,
                                Lists.list(), objectMapper.readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<List<CardListGetResponse>>() {
                                    }))
                            .handle(result))
                .andExpect(jsonPath("$", hasSize(3)));
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
