package com.server.bbo_gak.global.success;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {

    /**
     * 200 OK
     */
    OK_SUCCESS("성공입니다.", HttpStatus.OK.value()),

    /**
     * 201 CREATED
     */
    CREATED_SUCCESS("생성 성공입니다.", HttpStatus.CREATED.value());

    private final String message;
    private final int status;
}
