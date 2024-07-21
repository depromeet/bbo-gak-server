package com.server.bbo_gak.global.constant;

import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileExtension {
    JPEG("jpeg"),
    JPG("jpg"),
    PNG("png"),
    PDF("pdf"),
    ;

    private final String uploadExtension;

    public static FileExtension from(String uploadExtension) {
        return Arrays.stream(values())
            .filter(
                imageFileExtension ->
                    imageFileExtension.uploadExtension.equals(uploadExtension))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(ErrorCode.IMAGE_FILE_EXTENSION_NOT_FOUND));
    }
}
