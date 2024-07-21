package com.server.bbo_gak.domain.image.dto.request;

import com.server.bbo_gak.global.constant.FileExtension;


public record ImageUploadRequest(
    FileExtension fileExtension
) {

}
