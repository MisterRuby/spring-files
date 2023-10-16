package ruby.files.common.valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidExceptionController {

    public static final String BIND_EXCEPTION_MESSAGE = "타입에 맞지 않는 값이 존재합니다.";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ValidErrorResponse bindExceptionHandler(BindException e) {
        ValidErrorResponse validErrorResponse = ValidErrorResponse.builder()
                .message(BIND_EXCEPTION_MESSAGE)
                .build();

        e.getFieldErrors().forEach(validErrorResponse::addValidation);
        return validErrorResponse;
    }
}
