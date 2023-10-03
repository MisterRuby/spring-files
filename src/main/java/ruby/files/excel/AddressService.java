package ruby.files.excel;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.excel.enums.AddressDepthCode;
import ruby.files.excel.enums.AddressUseCode;
import ruby.files.excel.exception.AddressExcelUploadFailException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
        int useCol = 2;
        String cellValue = row.getCell(useCol).getStringCellValue();
        return AddressUseCode.USE.getCode().equals(cellValue);
    }

    private Address getAddressByRow(Row row) {
        String bcode = row.getCell(0).getStringCellValue();
        String addressName = row.getCell(1).getStringCellValue();
        byte depth = getDepth(bcode);

        return Address.builder()
            .bcode(bcode)
            .addressName(addressName)
            .depth(depth)
            .build();
    }

    private byte getDepth(String bcode) {
        if (AddressDepthCode.ONE.getSuffix().equals(bcode.substring(2))) {
            return AddressDepthCode.ONE.getDepth();
        }
        if (AddressDepthCode.TWO.getSuffix().equals(bcode.substring(5))) {
            return AddressDepthCode.TWO.getDepth();
        }

        return 3;
    }


    public void downloadExcel(HttpServletResponse response) throws IOException {
        String filename = URLEncoder.encode("주소목록.xlsx", StandardCharsets.UTF_8);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        List<Address> addresses = addressRepository.findAll();
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()){
            SXSSFSheet sheet = workbook.createSheet();
            setColumnWidth(sheet);

            CellStyle bcodeCellStyle = getBcodeCellStyle(workbook);
            CellStyle addressNameCellStyle = getAddressNameCellStyle(workbook);

            int rowNum = 0;
            for (Address address : addresses) {
                SXSSFRow row = sheet.createRow(rowNum++);

                SXSSFCell cell = row.createCell(0);
                cell.setCellValue(address.getBcode());
                cell.setCellStyle(bcodeCellStyle);

                cell = row.createCell(1);
                cell.setCellValue(address.getAddressName());
                cell.setCellStyle(addressNameCellStyle);
            }

            workbook.write(response.getOutputStream());
            workbook.dispose();
        }
    }

    private void setColumnWidth(SXSSFSheet sheet) {
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 10000);
    }

    private CellStyle getBcodeCellStyle(SXSSFWorkbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return cellStyle;
    }

    private CellStyle getAddressNameCellStyle(SXSSFWorkbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return cellStyle;
    }
}
