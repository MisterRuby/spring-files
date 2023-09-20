package ruby.files.common.file.exception;

public class FailDownloadFileException extends RuntimeException {

    private final static String MESSAGE = "파일 다운로드에 실패하였습니다.";

    public FailDownloadFileException() {
        super(MESSAGE);
    }
}
