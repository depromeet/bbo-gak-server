package com.server.bbo_gak.domain.user.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.server.bbo_gak.domain.user.dto.request.UserJobUpdateRequest;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.RestDocsFactory;
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
@Sql({"/all-data-delete.sql", "/user-test-data.sql"})
public class UserControllerTest extends AbstractRestDocsTests {
  private static final String DEFAULT_URL = "/api/v1/users";

  @Autowired
  private RestDocsFactory restDocsFactory;

  @Nested
  class 직군선택 {

    @Test
    public void 성공() throws Exception {

      //given
      UserJobUpdateRequest request = new UserJobUpdateRequest("개발자");
      //then
      mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL + "/job", request, HttpMethod.PUT,
              objectMapper))
          .andExpect(status().isOk())
          .andDo(restDocsFactory.getSuccessResource("[직군선택] 성공", "직군 선택", "auth", request, null));

    }
  }
}
