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
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.excel.enums.AddressDepthCode;
import ruby.files.excel.enums.AddressUseCode;
import ruby.files.excel.exception.AddressExcelUploadFailException;
import ruby.files.excel.exception.WrongAddressExcelFileException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AddressExcelService {

    private final AddressRepository addressRepository;

    public void uploadExcel(MultipartFile excelFile){
        addressRepository.deleteAll();
        try (InputStream is = excelFile.getInputStream()) {
            // SXSSF 는 쓰기만 가능
            XSSFWorkbook workbook = new XSSFWorkbook(is);

            XSSFSheet sheetAt = workbook.getSheetAt(0);
            if (!isAddressSheet(sheetAt)) {
                throw new WrongAddressExcelFileException();
            }
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

    private boolean isAddressSheet(XSSFSheet sheetAt) {
        for (Row row : sheetAt) {
            String bcode = row.getCell(0).getStringCellValue();
            String addressName = row.getCell(1).getStringCellValue();

            if (!isBcode(bcode) || !isAddressName(addressName)) {
                return false;
            }
        }

        return true;
    }

    private boolean isBcode(String bcode) {
        if (bcode == null || bcode.length() != 10) {
            return false;
        }

        try {
            Long.parseLong(bcode);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isAddressName(String addressName) {
        return !(addressName == null || addressName.isBlank());
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

    @Transactional(readOnly = true)
    public void downloadExcel(HttpServletResponse response) throws IOException {
        String filename = "주소목록.xlsx";
        setExcelResponseHeader(filename, response);

        List<Address> addresses = addressRepository.findAll();
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()){
            SXSSFSheet sheet = workbook.createSheet();
            setColumnWidth(sheet);
            List<CellStyle> colCellStyles = getColCellStyles(workbook);

            int rowNum = 0;
            for (Address address : addresses) {
                createRow(address, sheet, rowNum++, colCellStyles);
            }

            workbook.write(response.getOutputStream());
            workbook.dispose();
        }
    }

    private void setExcelResponseHeader(String filename, HttpServletResponse response) {
        String encodeFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodeFilename + "\"");
    }

    private List<CellStyle> getColCellStyles(SXSSFWorkbook workbook) {
        CellStyle bcodeCellStyle = getBcodeCellStyle(workbook);
        CellStyle addressNameCellStyle = getAddressNameCellStyle(workbook);
        return List.of(bcodeCellStyle, addressNameCellStyle);
    }

    private void createRow(Address address, SXSSFSheet sheet, int rowNum, List<CellStyle> colCellStyles) {
        SXSSFRow row = sheet.createRow(rowNum);

        SXSSFCell cell = row.createCell(0);
        cell.setCellValue(address.getBcode());
        cell.setCellStyle(colCellStyles.get(cell.getColumnIndex()));

        cell = row.createCell(1);
        cell.setCellValue(address.getAddressName());
        cell.setCellStyle(colCellStyles.get(cell.getColumnIndex()));
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
