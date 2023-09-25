package ruby.files.excel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.excel.dto.AddressExcelUploadDTO;

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
        MultipartFile file = excelUploadDTO.getFile();
        log.info("filename = {}", file.getOriginalFilename());

        addressService.uploadExcel(excelUploadDTO.getFile());
    }

    @GetMapping
    private List<Address> getList() {
        return addressRepository.findAll();
    }
}
