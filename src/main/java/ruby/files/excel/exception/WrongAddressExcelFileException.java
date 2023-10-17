package ruby.files.excel.exception;

import org.springframework.http.HttpStatus;
import ruby.files.common.exception.CustomRuntimeException;

public class WrongAddressExcelFileException extends CustomRuntimeException {

    public final static String MESSAGE = "업로드 한 주소 엑셀 파일 형식이 올바르지 않습니다.";

    public WrongAddressExcelFileException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
