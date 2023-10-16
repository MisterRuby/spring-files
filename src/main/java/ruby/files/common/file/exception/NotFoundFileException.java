package ruby.files.common.file.exception;

import org.springframework.http.HttpStatus;
import ruby.files.common.exception.CustomRuntimeException;

public class NotFoundFileException extends CustomRuntimeException {

    public final static String MESSAGE = "파일이 존재하지 않습니다.";

    public NotFoundFileException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
