package com.airdnb.global;

import com.airdnb.global.exception.BizException;
import com.airdnb.global.exception.ForbiddenException;
import com.airdnb.global.exception.NotFoundException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ApiResponse> handleArgumentException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();

        String rejectedValue = Objects.requireNonNull(fieldError).getField();
        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();

        String errorMessage = String.format("%s 검증에 실패하였습니다. (%s)", rejectedValue, defaultMessage);
        log.error(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(errorMessage));
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse> handleBizException(BizException e) {
        String errorMessage = e.getMessage();
        log.error(errorMessage, e);
        HttpStatus status = getHttpStatus(e);

        return ResponseEntity.status(status).body(ApiResponse.error(errorMessage));
    }

    private HttpStatus getHttpStatus(BizException e) {
        if (e instanceof NotFoundException) {
            return HttpStatus.NOT_FOUND;
        }
        if (e instanceof ForbiddenException) {
            return HttpStatus.FORBIDDEN;
        }
        return HttpStatus.BAD_REQUEST;
    }
}
