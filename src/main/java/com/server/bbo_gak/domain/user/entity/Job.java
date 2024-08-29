package com.server.bbo_gak.domain.user.entity;

import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Job {
    ALL("공통"),
    DESIGNER("디자이너"),
    DEVELOPER("개발자"),
    UNDEFINE("미설정");

    private final String value;

    public static Job findByValue(String value) {
        return Arrays.stream(Job.values())
            .filter(job -> job.getValue().equals(value))
            .findFirst()
            .orElseThrow((() -> new NotFoundException(ErrorCode.JOB_NOT_FOUND)));
    }
}
