package ruby.files.excel;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ruby.files.excel.dto.AddressExcelUploadDTO;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/addresses/excel")
public class AddressExcelController {

    private final AddressExcelService addressExcelService;

    @PostMapping
    public void upload(@Valid AddressExcelUploadDTO excelUploadDTO) {
        addressExcelService.uploadExcel(excelUploadDTO.getFile());
    }

    @GetMapping
    public void download(HttpServletResponse response) throws IOException {
        addressExcelService.downloadExcel(response);
    }
}
