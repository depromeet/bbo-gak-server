package com.server.bbo_gak.domain.image.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {

    @PostMapping("/presigned-url")
    public ResponseEntity<List<ImageResponse>> createPresignedUrlList(
        @Valid @RequestBody List<UploadImageRequest> requests) {
        return ResponseEntity.ok(imageService.getImageUploadUrlList(requests));
    }
}
