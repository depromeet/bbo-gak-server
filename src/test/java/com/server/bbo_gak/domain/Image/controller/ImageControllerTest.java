package com.server.bbo_gak.domain.Image.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.server.bbo_gak.domain.image.dto.request.ImageUploadRequest;
import com.server.bbo_gak.domain.image.dto.response.ImageUploadResponse;
import com.server.bbo_gak.domain.image.service.ImageService;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.constant.FileExtension;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@ActiveProfiles("test")
public class ImageControllerTest extends AbstractRestDocsTests {

    private static final String DEFAULT_URL = "/api/v1/images/presigned-urls";

    @MockBean
    private ImageService imageService;

    @Nested
    class PresignedUrls {

        @Test
        public void 성공() throws Exception {

            List<ImageUploadRequest> requests = List.of(
                new ImageUploadRequest(FileExtension.PNG),
                new ImageUploadRequest(FileExtension.PNG)
            );

            List<ImageUploadResponse> responses = List.of(
                new ImageUploadResponse("image1.jpg", "https://example.com/image1.jpg"),
                new ImageUploadResponse("image2.jpg", "https://example.com/image2.jpg")
            );

            when(imageService.createPresignedUrls(any(List.class), any(User.class))).thenReturn(responses);

            ResultActions resultActions = mockMvc.perform(
                    post(DEFAULT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests))
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(
                    MockMvcRestDocumentationWrapper.document("[presigned-urls] 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("presigned urls 생성")
                                .tags("image")
                                .requestSchema(Schema.schema("ImageUploadRequest"))
                                .responseSchema(Schema.schema("ImageUploadResponse"))
                                .requestFields(
                                    fieldWithPath("[].fileExtension").type(STRING).description("이미지 확장자")
                                )
                                .responseFields(
                                    fieldWithPath("[].filename").type(STRING).description("파일 이름"),
                                    fieldWithPath("[].presignedUrl").type(STRING).description("프리사인드 URL")
                                )
                                .build()
                        )
                    )
                );
        }

        @Test
        public void 유효하지않은_확장자_실패() throws Exception {
            List<ImageUploadRequest> invalidRequests = List.of(
                new ImageUploadRequest(FileExtension.PNG)
            );

            when(imageService.createPresignedUrls(any(List.class), any(User.class))).thenThrow(
                new BusinessException(ErrorCode.IMAGE_FILE_EXTENSION_NOT_FOUND));

            mockMvc.perform(
                    post(DEFAULT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequests))
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andDo(
                    MockMvcRestDocumentationWrapper.document("[presigned-urls] 실패-유효하지않은파일이름",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                            ResourceSnippetParameters.builder()
                                .description("presigned urls 생성")
                                .tags("image")
                                .requestSchema(Schema.schema("ImageUploadRequest"))
                                .responseSchema(Schema.schema("ErrorResponse"))
                                .requestFields(
                                    fieldWithPath("[].fileExtension").type(STRING).description("이미지 확장자")
                                )
                                .responseFields(
                                    fieldWithPath("message").type(STRING).description("에러 메시지"),
                                    fieldWithPath("status").type(STRING).description("HTTP 상태 코드")
                                )
                                .build()
                        )

                    )
                );
        }

    }
}
