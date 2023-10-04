package ruby.files.excel;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ruby.files.excel.dto.AddressExcelUploadDTO;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class AddressController {

    private final AddressRepository addressRepository;
    private final AddressService addressService;

    @PostMapping("/excel/upload")
    public void upload(AddressExcelUploadDTO excelUploadDTO) {
        addressService.uploadExcel(excelUploadDTO.getFile());
    }

    @GetMapping("/excel/download")
    public void download(HttpServletResponse response) throws IOException {
        addressService.downloadExcel(response);
    }

    @GetMapping
    private List<Address> getList() {
        return addressRepository.findAll();
    }
}
