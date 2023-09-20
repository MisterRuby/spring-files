package ruby.files.image.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
public class ImageMultipleUploadDTO {
    private List<MultipartFile> files;
}
