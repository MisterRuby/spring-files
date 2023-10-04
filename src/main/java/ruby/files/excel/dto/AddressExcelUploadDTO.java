package ruby.files.excel.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.common.valid.multipart.single.MultipartFileValid;

import static ruby.files.common.valid.multipart.MultipartFileType.EXCEL;

@Setter
@Getter
public class AddressExcelUploadDTO {
    @MultipartFileValid(types = {EXCEL})
    private MultipartFile file;
}
