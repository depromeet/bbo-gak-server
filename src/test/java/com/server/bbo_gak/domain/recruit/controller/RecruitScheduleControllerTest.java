package com.server.bbo_gak.domain.recruit.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.server.bbo_gak.domain.recruit.dto.request.RecruitScheduleCreateRequest;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitScheduleGetResponse;
import com.server.bbo_gak.domain.recruit.entity.RecruitScheduleStage;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.RestDocsFactory;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Sql({"/all-data-delete.sql", "/recruit-test-data.sql"})
class RecruitScheduleControllerTest extends AbstractRestDocsTests {

  private static final String DEFAULT_URL = "/api/v1/recruits/{id}/recruit-schedule";

  @Autowired
  private RestDocsFactory restDocsFactory;

  @Nested
  class 공고일정_생성 {

    @Test
    public void 성공() throws Exception {
      RecruitScheduleCreateRequest request = new RecruitScheduleCreateRequest(
          "1차 면접",
          "2024-08-31"
      );

      mockMvc.perform(
          restDocsFactory.createRequest(DEFAULT_URL, request, HttpMethod.POST, objectMapper, 1L))
          .andExpect(status().isOk())
          .andDo(restDocsFactory.getSuccessResource("[POST] 공고일정 생성 성공", "공고일정 생성", "RecruitSchedule", request, null));
    }
  }

  @Nested
  class 공고일정_전체조회 {

    // 첫 번째 response 객체
    RecruitScheduleGetResponse response1 = RecruitScheduleGetResponse.builder()
        .id(1L)
        .recruitScheduleStage(RecruitScheduleStage.CLOSING_DOCUMENT.getValue())
        .deadLine("2025-01-01T00:00:00")
        .build();

    // 두 번째 response 객체
    RecruitScheduleGetResponse response2 = RecruitScheduleGetResponse.builder()
        .id(2L)
        .recruitScheduleStage(RecruitScheduleStage.FIRST_INTERVIEW.getValue())
        .deadLine("2025-02-01T00:00:00")
        .build();

    @Test
    public void 성공() throws Exception {
      mockMvc.perform(
          restDocsFactory.createRequest(DEFAULT_URL, null, HttpMethod.GET, objectMapper, 1L))
          .andExpect(status().isOk())
          .andDo(restDocsFactory.getSuccessResourceList("[GET] 공고일정 전체 조회 성공", "공고일정 전체 조회 ", "RecruitSchedule", List.of(),
              List.of(response1, response2)));
    }

  }
}