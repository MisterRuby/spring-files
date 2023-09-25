package ruby.files.excel.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class AddressExcelUploadDTO {
    private MultipartFile file;
}
