package ruby.files.excel.exception;

import org.springframework.http.HttpStatus;
import ruby.files.common.exception.CustomRuntimeException;

public class AddressExcelUploadFailException extends CustomRuntimeException {

    public final static String MESSAGE = "주소 엑셀 파일 업로드가 실패하였습니다.";

    public AddressExcelUploadFailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
