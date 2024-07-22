package com.server.bbo_gak.domain.Image.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.server.bbo_gak.domain.image.dto.request.ImageUploadRequest;
import com.server.bbo_gak.domain.image.dto.response.ImageUploadResponse;
import com.server.bbo_gak.domain.image.service.ImageServiceImpl;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.constant.FileExtension;
import com.server.bbo_gak.global.utils.UUIDGenerator;
import com.server.bbo_gak.global.utils.s3.S3Util;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest {

    @Mock
    private S3Util s3Util;

    @InjectMocks
    private ImageServiceImpl imageService;

    @Mock
    private UUIDGenerator uuidGenerator;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        setField(user, "id", 1L);
    }

    @Test
    public void testCreatePresignedUrls() {
        List<ImageUploadRequest> requests = Stream.of(
            new ImageUploadRequest(FileExtension.PNG),
            new ImageUploadRequest(FileExtension.JPG)
        ).collect(Collectors.toList());

        when(s3Util.getS3PresignedUrl(anyString(), any())).thenAnswer(invocation -> {
            String fileName = invocation.getArgument(0);
            return "https://example.com/" + fileName;
        });
        when(uuidGenerator.generateUUID()).thenReturn("uuid");

        List<ImageUploadResponse> responses = imageService.createPresignedUrls(requests, user);

        assertEquals(2, responses.size());

        for (int i = 0; i < requests.size(); i++) {
            ImageUploadRequest request = requests.get(i);
            ImageUploadResponse response = responses.get(i);
            String expectedFileName =
                "card/" + user.getId() + "/uuid" + "." + request.fileExtension().getUploadExtension();
            assertEquals(expectedFileName, response.filename());
        }
    }
}
