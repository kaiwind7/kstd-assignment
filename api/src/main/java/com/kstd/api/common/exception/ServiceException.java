package com.kstd.api.common.exception;

import com.kstd.api.common.enums.ErrorCode;
import lombok.Getter;

public class ServiceException extends RuntimeException {
    @Getter
    private final ErrorCode errorCode;

    public ServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ServiceException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
