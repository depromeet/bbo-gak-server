package com.server.bbo_gak.domain.recruit.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.server.bbo_gak.domain.recruit.dao.RecruitRepository;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitCreateRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateSeasonRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateSiteUrlRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateStatusRequest;
import com.server.bbo_gak.domain.recruit.dto.request.RecruitUpdateTitleRequest;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitGetInnerResponse;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitGetResponse;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitGetTitleListResponse;
import com.server.bbo_gak.domain.recruit.dto.response.RecruitScheduleGetResponse;
import com.server.bbo_gak.domain.recruit.entity.RecruitScheduleStage;
import com.server.bbo_gak.domain.recruit.entity.RecruitStatus;
import com.server.bbo_gak.domain.user.entity.UserRole;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.RestDocsFactory;
import com.server.bbo_gak.global.security.PrincipalDetails;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Sql({"/all-data-delete.sql", "/recruit-test-data.sql"})
public class RecruitControllerTest extends AbstractRestDocsTests {

    private static final String DEFAULT_URL = "/api/v1/recruits";

    @Autowired
    private RestDocsFactory restDocsFactory;

    @Autowired
    private RecruitRepository recruitRepository;

    private RecruitGetResponse response;

    @BeforeEach
    void setUp() {
        response = RecruitGetResponse.builder()
            .id(1L)
            .title("New Title")
            .season("2024 상반기")
            .siteUrl("https://example.com")
            .recruitStatus(RecruitStatus.APPLICATION_COMPLETED.getValue())
            .createdDate("2023-01-01T00:00:00")
            .nearestSchedule(RecruitScheduleGetResponse.builder()
                .id(1L)
                .recruitScheduleStage(RecruitScheduleStage.CLOSING_DOCUMENT.getValue())
                .deadLine("2024-01-01T00:00:00")
                .build())
            .build();
    }

    @Nested
    class 공고제목_업데이트 {

        @Test
        public void 성공() throws Exception {
            RecruitUpdateTitleRequest request = new RecruitUpdateTitleRequest("New Title");

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/title", request, HttpMethod.PATCH, objectMapper, 1L))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResource("[PATCH] 제목 수정 성공", "공고 타이틀 수정", "Recruit", request,
                        response));
        }

        @Test
        public void 찾을수_없음_실패() throws Exception {
            RecruitUpdateTitleRequest request = new RecruitUpdateTitleRequest("New Title");

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/title", request, HttpMethod.PATCH,
                        objectMapper, 100L))
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

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/season", request, HttpMethod.PATCH, objectMapper,
                        1L))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResource("[PATCH] 분기 수정 성공", "공고 분기 수정", "Recruit", request, response));
        }

        @Test
        public void 찾을수_없음_실패() throws Exception {
            RecruitUpdateSeasonRequest request = new RecruitUpdateSeasonRequest("2024 상반기");

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/season", request, HttpMethod.PATCH, objectMapper,
                        100L))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[PATCH] 분기 수정 실패", "Recruit", request));
        }
    }

    @Nested
    class 공고타이틀_리스트_조회 {

        @Test
        public void 성공() throws Exception {

            RecruitGetTitleListResponse response = RecruitGetTitleListResponse.builder()
                .id(1L)
                .title("공고")
                .build();

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/titles", null, HttpMethod.GET, objectMapper,
                        1L))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResourceList("[GET] 공고 타이틀 최신순 5개 조회", "공고 타이틀 최신순 5개 가져오기", "Recruit",
                        List.of(),
                        List.of(response)));
        }

    }

    @Nested
    class 공고상태_업데이트 {

        @Test
        public void 성공() throws Exception {
            RecruitUpdateStatusRequest request =
                new RecruitUpdateStatusRequest(RecruitStatus.APPLICATION_COMPLETED.getValue());

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/status", request, HttpMethod.PATCH, objectMapper,
                        1L))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResource("[PATCH] 상태 수정 성공", "공고 상태 수정", "Recruit", request, response));
        }

        @Test
        public void 찾을수_없음_실패() throws Exception {
            RecruitUpdateStatusRequest request =
                new RecruitUpdateStatusRequest(RecruitStatus.APPLICATION_COMPLETED.getValue());

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/status", request, HttpMethod.PATCH, objectMapper,
                        100L))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[PATCH] 상태 수정 실패", "Recruit", request));
        }

        @Test
        public void 존재하지않는_상태_실패() throws Exception {
            RecruitUpdateStatusRequest request =
                new RecruitUpdateStatusRequest("지원 준비 중");

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/status", request, HttpMethod.PATCH, objectMapper,
                        100L))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[PATCH] 상태 수정 실패", "Recruit", request));
        }
    }

    @Nested
    class 공고사이트_URL_업데이트 {

        @Test
        public void 성공() throws Exception {
            RecruitUpdateSiteUrlRequest request = new RecruitUpdateSiteUrlRequest("https://example.com");

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/siteUrl", request, HttpMethod.PATCH, objectMapper,
                        1L))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResource("[PATCH] 사이트 URL 수정 성공", "공고 사이트 URL 수정", "Recruit", request,
                        response));
        }

        @Test
        public void 찾을수_없음_실패() throws Exception {
            RecruitUpdateSiteUrlRequest request = new RecruitUpdateSiteUrlRequest("https://example.com");

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}/siteUrl", request, HttpMethod.PATCH, objectMapper,
                        100L))
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
                RecruitScheduleStage.CLOSING_DOCUMENT.getValue(),
                "2024-12-31"
            );

            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL, request, HttpMethod.POST, objectMapper))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResource("[POST] 공고 생성 성공", "공고 생성", "Recruit", request,
                    response));
        }

        @Test
        public void 일정없음_성공() throws Exception {
            RecruitCreateRequest request = new RecruitCreateRequest(
                "2024 상반기",
                "New Recruit Title",
                "https://example.com",
                RecruitScheduleStage.CLOSING_DOCUMENT.getValue(),
                null
            );

            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL, request, HttpMethod.POST, objectMapper))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResource("[POST] 공고 생성 성공", "공고 생성", "Recruit", request,
                    response));
        }

        @Test
        public void 실패() throws Exception {
            RecruitCreateRequest request = new RecruitCreateRequest(
                "2024 상반기",
                "New Recruit Title",
                "https://example.com",
                "서류통과",
                "2024-12-31"
            );

            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL, request, HttpMethod.POST, objectMapper))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[POST] 공고 생성 실패", "Recruit", request));
        }
    }

    @Nested
    class 공고_조회 {

        RecruitGetInnerResponse response = RecruitGetInnerResponse.builder()
            .id(1L)
            .title("New Title")
            .season("2024 상반기")
            .siteUrl("https://example.com")
            .recruitStatus(RecruitStatus.APPLICATION_COMPLETED.getValue())
            .build();

        @Test
        public void 성공() throws Exception {
            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}", null, HttpMethod.GET, objectMapper, 1L))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResource("[GET] 공고 조회 성공", "공고 조회", "Recruit", null, response));

        }

        @Test
        public void 찾을수_없음_실패() throws Exception {

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}", null, HttpMethod.GET, objectMapper, 100L))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[GET] 공고 조회 실패", "Recruit", null));
        }
    }

    @Nested
    class 공고_삭제 {

        @Test
        public void 성공() throws Exception {
            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}", null, HttpMethod.DELETE, objectMapper, 1L))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResource("[DELETE] 공고 삭제 성공", "공고 삭제", "Recruit", null, null));

            assertTrue(recruitRepository.findById(1L).isEmpty());
        }

        @Test
        public void 찾을수_없음_실패() throws Exception {

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/{id}", null, HttpMethod.DELETE, objectMapper, 100L))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[DELETE] 공고 삭제 실패", "Recruit", null));
        }
    }

    @Nested
    class 공고_목록_조회 {

        @Test
        public void 전체_목록_조회_성공() throws Exception {

            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL, null, HttpMethod.GET, objectMapper))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResourceList("[GET] 전체 공고 목록 조회 성공", "전체 공고 목록 조회", "Recruit", List.of(),
                        List.of(response)));
        }

        @Test
        public void 시즌별_목록_조회_성공() throws Exception {

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
                            fieldWithPath("[].createdDate").type(JsonFieldType.STRING).description("생성일"),
                            fieldWithPath("[].nearestSchedule").type(JsonFieldType.OBJECT).description("가장 가까운 일정")
                                .optional(),
                            fieldWithPath("[].nearestSchedule.id").type(JsonFieldType.NUMBER).description("공고 ID"),
                            fieldWithPath("[].nearestSchedule.recruitScheduleStage").type(JsonFieldType.STRING)
                                .description("일정 단계"),
                            fieldWithPath("[].nearestSchedule.deadLine").type(JsonFieldType.STRING)
                                .description("마감일")

                        )

                        .build())));
        }
    }

    @Nested
    class 진행중인_목록_조회 {

        @Test
        public void 일정_등록_안된_공고_있을때() throws Exception {
            UserDetails userDetails = PrincipalDetails.ofJwt(2L, UserRole.USER);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/progressing", null, HttpMethod.GET, objectMapper))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResourceList("[GET]일정 등록 안된 공고 존재", "진행중 공고 목록 조회",
                    "Recruit",
                    List.of(), List.of(response)));
        }

        @Test
        public void 공고_하나에_여러개_일정_있을때() throws Exception {
            UserDetails userDetails = PrincipalDetails.ofJwt(3L, UserRole.USER);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/progressing", null, HttpMethod.GET, objectMapper))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResourceList("[GET]공고_하나에_여러개_일정_있을때", "진행중 공고 목록 조회",
                    "Recruit",
                    List.of(), List.of(response)));
        }

        @Test
        public void 성공() throws Exception {

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/progressing", null, HttpMethod.GET, objectMapper))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResourceList("[GET] 진행 중 공고 목록 조회 성공", "진행중 공고 목록 조회", "Recruit",
                    List.of(), List.of(response)));
        }
    }
}
