package ruby.files.image.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.common.valid.multipart.single.MultipartFileValid;

import java.io.Serializable;

import static ruby.files.common.valid.multipart.MultipartFileType.IMAGE;

@Setter
@Getter
@NoArgsConstructor
public class ImageUploadDTO implements Serializable {
    @MultipartFileValid(require = true, types = {IMAGE})
    private MultipartFile file;
}
