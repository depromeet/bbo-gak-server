package com.server.bbo_gak.domain.image.controller;

import com.server.bbo_gak.domain.image.dto.request.ImageUploadRequest;
import com.server.bbo_gak.domain.image.dto.response.ImageUploadResponse;
import com.server.bbo_gak.domain.image.service.ImageService;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.annotation.AuthUser;
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

    private final ImageService imageService;

    @PostMapping("/presigned-urls")
    public ResponseEntity<List<ImageUploadResponse>> getPresignedUrls(
        @RequestBody List<ImageUploadRequest> requests,
        @AuthUser User user
    ) {
        return ResponseEntity.ok(imageService.createPresignedUrls(requests, user));
    }
}
