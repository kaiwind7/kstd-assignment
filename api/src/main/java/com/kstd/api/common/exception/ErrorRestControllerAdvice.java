package com.kstd.api.common.exception;

import com.kstd.api.common.enums.ErrorCode;
import com.kstd.api.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorRestControllerAdvice {
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> serviceException(ServiceException e) {
        if (log.isDebugEnabled()) {
            log.debug("", e);
        } else {
            log.info("{}", e.getMessage());
        }
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(e));
    }
}
