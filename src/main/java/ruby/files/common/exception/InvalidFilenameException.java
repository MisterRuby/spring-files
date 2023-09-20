package ruby.files.common.exception;

public class InvalidFilenameException extends RuntimeException {

    private final static String MESSAGE = "파일 이름의 형식이 잘못되었습니다.";

    public InvalidFilenameException() {
        super(MESSAGE);
    }
}
