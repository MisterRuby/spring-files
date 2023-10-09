package ruby.files.common.file.exception;

public class FailDeleteFileException extends RuntimeException {

    private final static String MESSAGE = "파일 삭제에 실패했습니다.";

    public FailDeleteFileException() {
        super(MESSAGE);
    }
}
