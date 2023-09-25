package ruby.files.excel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.excel.exception.AddressExcelUploadFailException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public void uploadExcel(MultipartFile excelFile){
        try (InputStream is = excelFile.getInputStream()) {
            // SXSSF 는 쓰기만 가능
            XSSFWorkbook workbook = new XSSFWorkbook(is);

            XSSFSheet sheetAt = workbook.getSheetAt(0);

            for (Row row : sheetAt) {
                if (!isUseRow(row)) {
                    continue;
                }

                Address address = getAddressByRow(row);

                addressRepository.save(address);
            }
        } catch (IOException e) {
            throw new AddressExcelUploadFailException();
        }
    }

    private boolean isUseRow(Row row) {
        String USE_CODE = "Y";
        int useCol = 2;
        return USE_CODE.equals(row.getCell(useCol).getStringCellValue());
    }

    private Address getAddressByRow(Row row) {
        String bcode = row.getCell(0).getStringCellValue();
        String addressName = row.getCell(1).getStringCellValue();
        short depth = getDepth(bcode);

        return Address.builder()
            .bcode(bcode)
            .addressName(addressName)
            .depth(depth)
            .build();
    }

    private short getDepth(String bcode) {
        if ("00000000".equals(bcode.substring(2))) {
            return 1;
        }
        if ("00000".equals(bcode.substring(5))) {
            return 2;
        }

        return 3;
    }
}
