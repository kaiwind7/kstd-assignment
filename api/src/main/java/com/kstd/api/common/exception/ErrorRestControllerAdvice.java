package com.kstd.api.common.exception;

import com.kstd.api.common.enums.ErrorCode;
import com.kstd.api.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

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


    // 요청 본문에서 발생한 유효성 검사 예외 처리 (ex: @Valid, @RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("Validation failed: {}", e.getMessage());

        // 필드 에러 리스트 생성
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("Field '%s' %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(errors.toString())
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // 처리되지 않은 모든 예외에 대한 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unhandled exception: ", e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Internal server error")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
