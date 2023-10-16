package ruby.files.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ruby.files.common.exception.CustomRuntimeException;

/** 공통 예외처리를 위한 ControllerAdvice */
@RestControllerAdvice
public class CommonExceptionController {

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ErrorResponse> customRuntimeExceptionHandler(CustomRuntimeException e) {
        int statusCode = e.getStatusCode();
        ErrorResponse body = ErrorResponse.builder()
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(statusCode).body(body);
    }
}
