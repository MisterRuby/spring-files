package ruby.files.image.exception;

public class ImageTypeException extends RuntimeException {

    private final static String MESSAGE = "파일 형식이 이미지가 아닙니다.";

    public ImageTypeException() {
        super(MESSAGE);
    }
}
