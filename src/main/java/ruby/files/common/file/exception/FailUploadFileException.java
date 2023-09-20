package ruby.files.common.file.exception;

public class FailUploadFileException extends RuntimeException {

    private final static String MESSAGE = "파일 업로드에 실패하였습니다.";

    public FailUploadFileException() {
        super(MESSAGE);
    }
}
