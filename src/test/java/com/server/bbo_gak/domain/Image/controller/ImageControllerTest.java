package com.server.bbo_gak.domain.Image.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.server.bbo_gak.domain.image.dto.request.ImageUploadRequest;
import com.server.bbo_gak.domain.image.dto.response.ImageUploadResponse;
import com.server.bbo_gak.domain.image.service.ImageService;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.RestDocsFactory;
import com.server.bbo_gak.global.constant.FileExtension;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ImageControllerTest extends AbstractRestDocsTests {

    private static final String DEFAULT_URL = "/api/v1/images/presigned-urls";

    @Autowired
    private RestDocsFactory restDocsFactory;

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

            //then
            mockMvc.perform(restDocsFactory.createRequestList(DEFAULT_URL, requests, HttpMethod.POST,
                    objectMapper))
                .andExpect(status().isOk())
                .andDo(restDocsFactory.getSuccessResourceList("[presigned-urls] 성공", "presigend url 생성", "image",
                    requests,
                    responses));
        }

        @Test
        public void 유효하지않은_확장자_실패() throws Exception {
            List<ImageUploadRequest> invalidRequests = List.of(
                new ImageUploadRequest(FileExtension.PNG)
            );

            when(imageService.createPresignedUrls(any(List.class), any(User.class))).thenThrow(
                new BusinessException(ErrorCode.IMAGE_FILE_EXTENSION_NOT_FOUND));

            //then
            mockMvc.perform(restDocsFactory.createRequestList(DEFAULT_URL, invalidRequests, HttpMethod.POST,
                    objectMapper))
                .andExpect(status().isBadRequest())
                .andDo(
                    restDocsFactory.getFailureResourceList("[presigned-urls] 실패-유효하지않은파일이름", "image", invalidRequests));
        }
    }
}
