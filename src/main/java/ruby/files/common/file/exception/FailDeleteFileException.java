package ruby.files.common.file.exception;

import org.springframework.http.HttpStatus;
import ruby.files.common.exception.CustomRuntimeException;

public class FailDeleteFileException extends CustomRuntimeException {

    public final static String MESSAGE = "파일 삭제에 실패했습니다.";

    public FailDeleteFileException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
