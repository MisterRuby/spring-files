package ruby.files.common.file.exception;

import org.springframework.http.HttpStatus;
import ruby.files.common.exception.CustomRuntimeException;

public class InvalidFileTypeException extends CustomRuntimeException {

    public final static String MESSAGE = "허용되지 않는 형식의 파일입니다.";

    public InvalidFileTypeException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
