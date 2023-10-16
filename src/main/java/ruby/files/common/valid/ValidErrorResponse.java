package ruby.files.common.valid;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ValidErrorResponse {

    private final String message;
    private Map<String, String> validation;

    @Builder
    public ValidErrorResponse(String message) {
        this.message = message;
    }

    public void addValidation(FieldError fieldError) {
        if (validation == null) this.validation = new HashMap<>();

        this.validation.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
