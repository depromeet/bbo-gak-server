package com.server.bbo_gak.domain.recruit.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitCreateRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateSeasonRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateSiteUrlRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateStatusRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateTitleRequest;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitGetResponse;
import com.server.bbo_gak.domain.recruit.entity.RecruitScheduleStage;
import com.server.bbo_gak.domain.recruit.entity.RecruitStatus;
import com.server.bbo_gak.domain.recruit.service.RecruitService;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.RestDocsFactory;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class RecruitControllerTest extends AbstractRestDocsTests {

    private static final String DEFAULT_URL = "/api/v1/recruits";

    @MockBean
    private RecruitService recruitService;

    @Autowired
    private RestDocsFactory restDocsFactory;

    private RecruitGetResponse response;
    private User user;

    @BeforeEach
    void setUp() {
        response = RecruitGetResponse.builder()
            .id(1L)
            .title("New Title")
            .season("2024 상반기")
            .siteUrl("https://example.com")
            .recruitStatus(RecruitStatus.APPLICATION_COMPLETED.getValue())
            .createdDate("2023-01-01T00:00:00")
            .build();
        user = User.builder().build(); /* initialize fields */
    }

    @Nested
    class 공고제목_업데이트 {

        @Test
        public void 성공() throws Exception {
            RecruitUpdateTitleRequest request = new RecruitUpdateTitleRequest("New Title");
            when(recruitService.updateRecruitTitle(any(), any(), any())).thenReturn(response);

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/title", request, HttpMethod.PATCH, objectMapper, 1L))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResource("[PATCH] 제목 수정 성공", "공고 타이틀 수정", "Recruit", request,
                        response));
        }

        @Test
        public void 실패() throws Exception {
            RecruitUpdateTitleRequest request = new RecruitUpdateTitleRequest("New Title");
            when(recruitService.updateRecruitTitle(any(), any(), any())).thenThrow(
                new BusinessException(ErrorCode.RECRUIT_NOT_FOUND));

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/title", request, HttpMethod.PATCH, objectMapper, 1L))
                .andExpect(status().isNotFound())
                .andDo(
                    restDocsFactory.getFailureResource("[PATCH] 제목 수정 실패", "Recruit", request));
        }
    }

    @Nested
    class 공고시즌_업데이트 {

        @Test
        public void 성공() throws Exception {
            RecruitUpdateSeasonRequest request = new RecruitUpdateSeasonRequest("2024 상반기");
            when(recruitService.updateRecruitSeason(any(), any(), any())).thenReturn(response);

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/season", request, HttpMethod.PATCH, objectMapper,
                        1L))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResource("[PATCH] 분기 수정 성공", "공고 분기 수정", "Recruit", request, response));
        }

        @Test
        public void 실패() throws Exception {
            RecruitUpdateSeasonRequest request = new RecruitUpdateSeasonRequest("2024 상반기");
            when(recruitService.updateRecruitSeason(any(), any(), any())).thenThrow(
                new BusinessException(ErrorCode.RECRUIT_NOT_FOUND));

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/season", request, HttpMethod.PATCH, objectMapper,
                        1L))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[PATCH] 분기 수정 실패", "Recruit", request));
        }
    }

    @Nested
    class 공고상태_업데이트 {

        @Test
        public void 성공() throws Exception {
            RecruitUpdateStatusRequest request = new RecruitUpdateStatusRequest(RecruitStatus.APPLICATION_COMPLETED);
            when(recruitService.updateRecruitStatus(any(), any(), any())).thenReturn(response);

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/status", request, HttpMethod.PATCH, objectMapper,
                        1L))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResource("[PATCH] 상태 수정 성공", "공고 상태 수정", "Recruit", request, response));
        }

        @Test
        public void 실패() throws Exception {
            RecruitUpdateStatusRequest request = new RecruitUpdateStatusRequest(RecruitStatus.APPLICATION_COMPLETED);
            when(recruitService.updateRecruitStatus(any(), any(), any())).thenThrow(
                new BusinessException(ErrorCode.RECRUIT_NOT_FOUND));

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/status", request, HttpMethod.PATCH, objectMapper,
                        1L))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[PATCH] 상태 수정 실패", "Recruit", request));
        }
    }

    @Nested
    class 공고사이트_URL_업데이트 {

        @Test
        public void 성공() throws Exception {
            RecruitUpdateSiteUrlRequest request = new RecruitUpdateSiteUrlRequest("https://example.com");
            when(recruitService.updateRecruitSiteUrl(any(), any(), any())).thenReturn(response);

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/siteUrl", request, HttpMethod.PATCH, objectMapper,
                        1L))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResource("[PATCH] 사이트 URL 수정 성공", "공고 사이트 URL 수정", "Recruit", request,
                        response));
        }

        @Test
        public void 실패() throws Exception {
            RecruitUpdateSiteUrlRequest request = new RecruitUpdateSiteUrlRequest("https://example.com");
            when(recruitService.updateRecruitSiteUrl(any(), any(), any())).thenThrow(
                new BusinessException(ErrorCode.RECRUIT_NOT_FOUND));

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/siteUrl", request, HttpMethod.PATCH, objectMapper,
                        1L))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[PATCH] 사이트 URL 수정 실패", "Recruit", request));
        }
    }

    @Nested
    class 공고_생성 {

        @Test
        public void 성공() throws Exception {
            RecruitCreateRequest request = new RecruitCreateRequest(
                "2024 상반기",
                "New Recruit Title",
                "https://example.com",
                RecruitScheduleStage.CLOSING_DOCUMENT,
                "2024-12-31"
            );
            RecruitGetResponse response = RecruitGetResponse.builder()
                .id(1L)
                .title("New Title")
                .season("2024 상반기")
                .siteUrl("https://example.com")
                .recruitStatus("")
                .createdDate("2023-01-01T00:00:00")
                .build();

            when(recruitService.createRecruit(any(), any())).thenReturn(response);

            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL, request, HttpMethod.POST, objectMapper))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResource("[POST] 공고 생성 성공", "공고 생성", "Recruit", request,
                    response));
        }
    }

    @Nested
    class 공고_삭제 {

        @Test
        public void 성공() throws Exception {
            doNothing().when(recruitService).deleteRecruit(any(), any());

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}", null, HttpMethod.DELETE, objectMapper, 1L))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResource("[DELETE] 공고 삭제 성공", "공고 삭제", "Recruit", null, null));
        }

        @Test
        public void 채용_삭제_실패() throws Exception {
            doThrow(new NotFoundException(ErrorCode.RECRUIT_NOT_FOUND)).when(recruitService)
                .deleteRecruit(any(), any());

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}", null, HttpMethod.DELETE, objectMapper, 1L))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[DELETE] 공고 삭제 실패", "Recruit", null));
        }
    }

    @Nested
    class 공고_목록_조회 {

        @Test
        public void 전체_목록_조회_성공() throws Exception {
            when(recruitService.getTotalRecruitList(any())).thenReturn(List.of(response));

            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL, null, HttpMethod.GET, objectMapper))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResourceList("[GET] 전체 공고 목록 조회 성공", "전체 공고 목록 조회", "Recruit", List.of(),
                        List.of(response)));
        }

        @Test
        public void 시즌별_목록_조회_성공() throws Exception {
            when(recruitService.getRecruitListBySeason(any(), any())).thenReturn(List.of(response));

            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL + "/bySeason", null, HttpMethod.GET, objectMapper)
                    .queryParam("season", "2024 상반기"))
                .andExpect(status().isOk())
                .andDo(document("[GET] 분기별 공고 리스트 조회 성공", preprocessResponse(prettyPrint()), resource(
                    ResourceSnippetParameters.builder().description("분기별 공고 목록 조회").tags("Recruit")
                        .queryParameters(parameterWithName("season").description("2024 상반기"))
                        .responseSchema(Schema.schema("RecruitGetResponse"))
                        .responseFields(fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("ID"),
                            fieldWithPath("[].title").type(JsonFieldType.STRING).description("제목"),
                            fieldWithPath("[].season").type(JsonFieldType.STRING).description("분기"),
                            fieldWithPath("[].siteUrl").type(JsonFieldType.STRING).description("사이트 url"),
                            fieldWithPath("[].recruitStatus").type(JsonFieldType.STRING).description("지원 상태"),
                            fieldWithPath("[].createdDate").type(JsonFieldType.STRING).description("생성일"))
                        .build())));
        }

        @Test
        public void 진행중인_목록_조회_성공() throws Exception {
            when(recruitService.getProgressingRecruitList(any())).thenReturn(List.of(response));

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/progressing", null, HttpMethod.GET, objectMapper))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResourceList("[GET] 진행 중 공고 목록 조회 성공", "진행중 공고 목록 조회", "Recruit",
                    List.of(), List.of(response)));
        }
    }
}
