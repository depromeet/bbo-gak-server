package com.server.bbo_gak.domain.image.service;

import com.amazonaws.HttpMethod;
import com.server.bbo_gak.domain.image.dto.request.ImageUploadRequest;
import com.server.bbo_gak.domain.image.dto.response.ImageUploadResponse;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.constant.FileExtension;
import com.server.bbo_gak.global.utils.UUIDGenerator;
import com.server.bbo_gak.global.utils.s3.S3Util;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final S3Util s3Util;
    private final UUIDGenerator uuidGenerator;

    @Override
    @Transactional
    public List<ImageUploadResponse> createPresignedUrls(List<ImageUploadRequest> requests, User user) {

        return requests.stream()
            .map(request -> getImageUploadResponse(
                user.getId(),
                request.fileExtension(),
                uuidGenerator.generateUUID()))
            .collect(Collectors.toList());
    }

    private ImageUploadResponse getImageUploadResponse(Long memberId, FileExtension extension, String imageKey) {
        String fileName = createImageFileName(
            memberId,
            imageKey,
            extension
        );

        String presignedUrl = s3Util.getS3PresignedUrl(fileName, HttpMethod.PUT);

        return ImageUploadResponse.of(fileName, presignedUrl);
    }

    private String createImageFileName(
        Long memberId,
        String imageKey,
        FileExtension fileExtension
    ) {
        return "card"
            +
            "/"
            +
            memberId
            +
            "/"
            +
            imageKey
            + "."
            + fileExtension.getUploadExtension();
    }
}
