package com.server.bbo_gak.global.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "Test"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류, 관리자에게 문의하세요"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 엔티티를 찾을 수 없습니다."),


    //Auth
    AUTH_NOT_FOUND(HttpStatus.UNAUTHORIZED, "시큐리티 인증 정보를 찾을 수 없습니다."),
    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "알 수 없는 에러"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 access Token입니다"),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 refresh Token입니다. 재로그인하세요"),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 길이 및 형식이 다른 Token입니다"),
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED, "서명이 잘못된 토큰입니다."),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "토큰이 없습니다"),
    TOKEN_SUBJECT_FORMAT_ERROR(HttpStatus.UNAUTHORIZED, "Subject 값에 Long 타입이 아닌 다른 타입이 들어있습니다."),
    AT_EXPIRED_AND_RT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AT는 만료되었고 RT는 비어있습니다."),

    PASSWORD_NOT_MATCHES(HttpStatus.BAD_REQUEST, "비밀번호를 잘못 입력하셨습니다."),

    //User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),

    //Image
    IMAGE_FILE_EXTENSION_NOT_FOUND(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 확장자입니다."),
    IMAGE_FILE_NOT_FOUND_IN_S3(HttpStatus.NOT_FOUND, "해당 파일은 S3내에 존재하지 않습니다"),

    //Card
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카드를 찾을 수 없습니다"),
    CARD_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카드 타입을 찾을 수 없습니다"),

    //Tag
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 태그를 찾을 수 없습니다"),

    TAG_DUPLICATED(HttpStatus.BAD_REQUEST, "해당 태그가 이미 추가 돼있습니다."),

    //Season
    SEASON_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 공고 분기를 찾을 수 없습니다"),

    //Recruit
    RECRUIT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 공고를 찾을 수 없습니다"),

    CARD_TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카드와 태그 매핑을 찾을 수 없습니다"),

    //CardMemo
    CARD_MEMO_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카드를 찾을 수 없습니다");


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
