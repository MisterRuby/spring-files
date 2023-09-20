package ruby.files.image.exception;

public class NotFoundFileException extends RuntimeException {

    private final static String MESSAGE = "파일 정보를 찾을 수 없습니다.";

    public NotFoundFileException() {
        super(MESSAGE);
    }
}
