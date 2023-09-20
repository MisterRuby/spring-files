package ruby.files.image.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ImageUploadDTO {
    private MultipartFile file;
}
