package com.kstd.api.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("에러가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ALREADY_REGISTERED_USER("이미 등록된 사용자입니다.", HttpStatus.CONFLICT),
    OVER_CAPACITY("강의 수용 인원을 초과하였습니다.", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST("", HttpStatus.BAD_REQUEST),
    NOT_FOUND_ENTITY("대상을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
