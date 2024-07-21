package com.server.bbo_gak.domain.image.service;

import com.server.bbo_gak.domain.image.dto.request.ImageUploadRequest;
import com.server.bbo_gak.domain.image.dto.response.ImageUploadResponse;
import com.server.bbo_gak.domain.user.entity.User;
import java.util.List;

public interface ImageService {

    List<ImageUploadResponse> createPresignedUrls(List<ImageUploadRequest> request, User user);
}
