package ruby.files.common.file.exception;

import org.springframework.http.HttpStatus;
import ruby.files.common.exception.CustomRuntimeException;

public class FailUploadFileException extends CustomRuntimeException {

    public final static String MESSAGE = "파일 업로드에 실패하였습니다.";

    public FailUploadFileException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
