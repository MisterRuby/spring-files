package ruby.files.common.exception;

public class InvalidFileTypeException extends RuntimeException {

    private final static String MESSAGE = "허용되지 않는 형식의 파일입니다.";

    public InvalidFileTypeException() {
        super(MESSAGE);
    }
}
