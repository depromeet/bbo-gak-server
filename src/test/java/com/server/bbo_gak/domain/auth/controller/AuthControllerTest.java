package com.server.bbo_gak.domain.auth.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.server.bbo_gak.domain.auth.dto.request.LoginRequest;
import com.server.bbo_gak.domain.auth.service.AuthService;
import com.server.bbo_gak.domain.user.entity.UserRepository;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.security.jwt.dto.TokenDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AuthControllerTest extends AbstractRestDocsTests {

    private static final String DEFAULT_URL = "/api/v1/users/test";
    @MockBean
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;


    @Test
    public void createUser() throws Exception {
        TokenDto tokenDto = TokenDto.builder()
            .accessToken("accessToken")
            .refreshToken("refreshToken")
            .build();

        LoginRequest loginRequest = new LoginRequest("loginId", "password");

        when(authService.login(any(LoginRequest.class))).thenReturn(tokenDto);

        //then
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.post(DEFAULT_URL + "/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)) // 요청 본문 추가
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andDo(
                MockMvcRestDocumentationWrapper.document("login",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(
                        ResourceSnippetParameters.builder()
                            .description("로그인 테스트-jwt 토큰 생성")
                            .requestSchema(Schema.schema("LoginRequest.Post"))
                            .responseSchema(Schema.schema("TokenDto.Post"))
                            .requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 id"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                            )
                            .responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("access token"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("refresh token")
                            )
                            .build()
                    )
                )
            );
    }
}
