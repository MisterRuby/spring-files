package ruby.files.common.file.exception;

public class NotFoundFileException extends RuntimeException {

    private final static String MESSAGE = "파일이 존재하지 않습니다.";

    public NotFoundFileException() {
        super(MESSAGE);
    }
}
