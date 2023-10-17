package ruby.files.excel;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ruby.files.common.CommonExceptionController;
import ruby.files.common.valid.ValidExceptionController;
import ruby.files.excel.exception.WrongAddressExcelFileException;

import java.io.File;
import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {
    AddressExcelController.class, AddressExcelService.class,
    DefaultResourceLoader.class,
    ValidExceptionController.class, CommonExceptionController.class
})
@EnableAutoConfiguration
@AutoConfigureMockMvc
@Transactional
class AddressExcelControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    EntityManager em;
    @Autowired
    AddressRepository addressRepository;

    @BeforeEach
    void beforeEach() {
        addressRepository.deleteAll();
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("형식에 맞지 않는 엑셀 업로드")
    void failWrongAddressExcelUpload() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:static");
        File file = new File(resource.getFile().getAbsolutePath() + "/file/excelSample.xlsx");
        String fieldName = "file";
        String originalFilename = "excelSample.xlsx";
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        FileInputStream fileInputStream = new FileInputStream(file);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(fieldName, originalFilename, contentType, fileInputStream);

        mockMvc.perform(multipart("/addresses/excel")
                .file(mockMultipartFile)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(WrongAddressExcelFileException.MESSAGE));
    }

    @Test
    @DisplayName("주소 목록 엑셀 업로드 성공")
    void successAddressExcelUpload() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:static");
        File file = new File(resource.getFile().getAbsolutePath() + "/file/addressCode.xlsx");
        String fieldName = "file";
        String originalFilename = "addressCode.xlsx";
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        FileInputStream fileInputStream = new FileInputStream(file);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(fieldName, originalFilename, contentType, fileInputStream);

        mockMvc.perform(multipart("/addresses/excel")
                .file(mockMultipartFile)
            )
            .andExpect(status().isOk());

        long count = addressRepository.count();
        assertThat(count).isGreaterThan(0);
    }

    @Test
    @DisplayName("주소 목록 엑셀 다운로드 성공")
    void successAddressExcelDownload() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:static");
        File file = new File(resource.getFile().getAbsolutePath() + "/file/addressCode.xlsx");
        String fieldName = "file";
        String originalFilename = "addressCode.xlsx";
        String contentType = "application/vnd.ms-excel";
        FileInputStream fileInputStream = new FileInputStream(file);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(fieldName, originalFilename, contentType, fileInputStream);

        mockMvc.perform(multipart("/addresses/excel")
                .file(mockMultipartFile)
            );

        mockMvc.perform(get("/addresses/excel"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/vnd.ms-excel"));
    }
}