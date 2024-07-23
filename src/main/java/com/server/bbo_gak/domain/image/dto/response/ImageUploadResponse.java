package com.server.bbo_gak.domain.image.dto.response;

import lombok.Builder;

@Builder
public record ImageUploadResponse(
    String filename,
    String presignedUrl
) {

    public static ImageUploadResponse of(String filename, String presignedUrl) {
        return ImageUploadResponse.builder()
            .filename(filename)
            .presignedUrl(presignedUrl)
            .build();
    }

}
