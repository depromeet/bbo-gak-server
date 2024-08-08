package com.server.bbo_gak.domain.auth.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.server.bbo_gak.domain.auth.dto.request.LoginRequest;
import com.server.bbo_gak.domain.auth.dto.response.LoginResponse;
import com.server.bbo_gak.domain.auth.service.AuthService;
import com.server.bbo_gak.domain.user.entity.OauthProvider;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.RestDocsFactory;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import com.server.bbo_gak.global.security.jwt.dto.TokenDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AuthControllerTest extends AbstractRestDocsTests {

    private static final String DEFAULT_URL = "/api/v1/users/test";
    @MockBean
    private AuthService authService;

    @Autowired
    private RestDocsFactory restDocsFactory;

    @Nested
    class 소셜_로그인 {

        @Test
        public void 성공() throws Exception {
            //given
            String socialAccessToken = "sampleToken";
            OauthProvider provider = OauthProvider.GOOGLE;
            LoginResponse mockLoginResponse = LoginResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            when(authService.socialLogin(socialAccessToken, provider)).thenReturn(mockLoginResponse);

            //when
            mockMvc.perform(post("/api/v1/users/social-login")
                    .header("SOCIAL-AUTH-TOKEN", socialAccessToken)
                    .param("provider", provider.name())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))

                //then
                // Verifying HTTP request matching
                .andExpect(status().isOk())
                // 헤더 검증 - requestBody의 content-type
                .andExpect(
                    header().string(HttpHeaders.CONTENT_TYPE, matchesPattern("application/json(;charset=UTF-8)?")))
                // Verifying output serialization
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value("accessToken")) //$로 JSON 루트 객체 접근
                .andExpect(jsonPath("$.refreshToken").value("refreshToken")) //

                // RestDocs 문서
                .andDo(document("[소셜 로그인] 성공",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(
                        ResourceSnippetParameters.builder()
                            .tags("auth")  // API 문서의 태그
                            .description("소셜로그인 - jwt 토큰 생성")  // API 문서의 설명
                            .requestHeaders(
                                headerWithName("SOCIAL-AUTH-TOKEN").description(
                                    "소셜 엑세스 토큰")
                            )
                            .queryParameters(
                                parameterWithName("provider").description("OAuth provider(ex) GOOGLE)")
                            )
                            .responseFields(  // 응답 필드
                                fieldWithPath("accessToken").description("accessToken"),
                                fieldWithPath("refreshToken").description("refreshToken")
                            )
                            .build()
                    )));


        }
    }

    @Nested
    class 로그인 {

        @Test
        public void 성공() throws Exception {
            TokenDto tokenDto = TokenDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

            LoginRequest loginRequest = new LoginRequest("test", "test123");

            when(authService.login(any(LoginRequest.class))).thenReturn(tokenDto);

            //then
            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL + "/login", loginRequest, HttpMethod.POST,
                    objectMapper))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResource("[로그인] 성공", "jwt 토큰 생성", "auth", loginRequest, tokenDto));
        }

        @Test
        public void 비밀번호_실패() throws Exception {

            // Invalid request (missing loginId)
            LoginRequest invalidLoginRequest = new LoginRequest("test", "wrong");
            when(authService.login(any(LoginRequest.class))).thenThrow(
                new BusinessException(ErrorCode.PASSWORD_NOT_MATCHES));

            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL + "/login", invalidLoginRequest, HttpMethod.POST,
                    objectMapper))
                .andExpect(status().isBadRequest())
                .andDo(restDocsFactory.getFailureResource("[로그인] 비밀번호_실패", "auth", invalidLoginRequest));
        }

        @Test
        public void 아이디_없음() throws Exception {

            // Invalid request (missing loginId)
            LoginRequest invalidLoginRequest = new LoginRequest("wrong", "test123");
            when(authService.login(any(LoginRequest.class))).thenThrow(
                new NotFoundException(ErrorCode.USER_NOT_FOUND));

            mockMvc.perform(restDocsFactory.createRequest(DEFAULT_URL + "/login", invalidLoginRequest, HttpMethod.POST,
                    objectMapper))
                .andExpect(status().isNotFound())
                .andDo(restDocsFactory.getFailureResource("[로그인] 아이디_없음", "auth", invalidLoginRequest));

        }
    }

}
