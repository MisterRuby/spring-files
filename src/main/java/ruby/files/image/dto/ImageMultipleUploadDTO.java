package ruby.files.image.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.common.valid.multipart.multiple.MultipartFileMultipleValid;

import java.io.Serializable;
import java.util.List;

import static ruby.files.common.valid.multipart.MultipartFileType.IMAGE;

@Setter
@Getter
public class ImageMultipleUploadDTO implements Serializable {
    @MultipartFileMultipleValid(types = {IMAGE})
    private List<MultipartFile> files;
}
