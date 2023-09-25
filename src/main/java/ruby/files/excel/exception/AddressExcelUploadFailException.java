package ruby.files.excel.exception;

public class AddressExcelUploadFailException extends RuntimeException {

    private final static String MESSAGE = "주소 엑셀 파일 업로드가 실패하였습니다.";

    public AddressExcelUploadFailException() {
        super(MESSAGE);
    }
}
