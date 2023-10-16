package ruby.files.common.file.exception;

import org.springframework.http.HttpStatus;
import ruby.files.common.exception.CustomRuntimeException;

public class InvalidFilenameException extends CustomRuntimeException {

    public final static String MESSAGE = "파일 이름의 형식이 잘못되었습니다.";

    public InvalidFilenameException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
