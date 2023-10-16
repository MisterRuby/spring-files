package ruby.files.image;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ruby.files.common.file.local.LocalFileService;
import ruby.files.common.valid.ValidExceptionController;
import ruby.files.common.valid.multipart.multiple.MultipartFileMultipleValid;
import ruby.files.common.valid.multipart.single.MultipartFileValid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {
    ImageController.class, ImageService.class,
    DefaultResourceLoader.class, LocalFileService.class,
    ValidExceptionController.class
})
@EnableAutoConfiguration
@AutoConfigureMockMvc
@Transactional
class ImageControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    ImageRepository imageRepository;

    @Test
    @DisplayName("이미지 단일 업로드시 파일 명이 빈 값일 경우")
    void validEmptyFilenameSingleImageUpload() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:static");
        File file = new File(resource.getFile().getAbsolutePath() + "/file/imageSample.jpg");
        String fieldName = "file";
        String originalFilename = "";
        String contentType = "image/png";
        FileInputStream fileInputStream = new FileInputStream(file);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(fieldName, originalFilename, contentType, fileInputStream);

        mockMvc.perform(multipart("/images")
                .file(mockMultipartFile)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validation.file").value(MultipartFileValid.FILENAME_EMPTY_MESSAGE));
    }

    @Test
    @DisplayName("이미지 단일 업로드시 파일 타입이 잘못된 경우")
    void validWrongContentTypeSingleImageUpload() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:static");
        File file = new File(resource.getFile().getAbsolutePath() + "/file/imageSample.jpg");
        String fieldName = "file";
        String originalFilename = "imageSample.jpg";
        String contentType = "application/vnd.ms-excel";
        FileInputStream fileInputStream = new FileInputStream(file);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(fieldName, originalFilename, contentType, fileInputStream);

        mockMvc.perform(multipart("/images")
                .file(mockMultipartFile)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validation.file").value(MultipartFileValid.MESSAGE));
    }

    @Test
    @DisplayName("이미지 단일 업로드 성공")
    void successSingleImageUpload() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:static");
        File file = new File(resource.getFile().getAbsolutePath() + "/file/imageSample.jpg");
        String fieldName = "file";
        String originalFilename = "imageSample.jpg";
        String contentType = "image/png";
        FileInputStream fileInputStream = new FileInputStream(file);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(fieldName, originalFilename, contentType, fileInputStream);

        mockMvc.perform(multipart("/images")
                .file(mockMultipartFile)
            )
            .andExpect(status().isOk());

        Image image = imageRepository.findAll().get(0);
        String filePath = resource.getFile().getAbsolutePath() + File.separator + ImageService.IMAGE_DIR
            + File.separator + image.getSaveFilename();
        file = new File(filePath);
        assertThat(imageRepository.count()).isEqualTo(1);
        assertThat(image.getOriginalFilename()).isEqualTo(originalFilename);
        assertThat(file.exists()).isTrue();
    }

    @Test
    @DisplayName("이미지 멀티 전송시 파일 명이 빈 값인 파일이 있는 경우")
    void validEmptyFilenameMultipleImageUpload() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:static");
        File file1 = new File(resource.getFile().getAbsolutePath() + "/file/imageSample.jpg");
        String fieldName1 = "files";
        String originalFilename1 = "rybvy.png";
        String contentType1 = "image/png";
        FileInputStream fileInputStream1 = new FileInputStream(file1);
        MockMultipartFile mockMultipartFile1 = new MockMultipartFile(fieldName1, originalFilename1, contentType1, fileInputStream1);

        File file2 = new File(resource.getFile().getAbsolutePath() + "/file/imageSample.jpg");
        String fieldName2 = "files";
        String originalFilename2 = "";
        String contentType2 = "image/png";
        FileInputStream fileInputStream2 = new FileInputStream(file2);
        MockMultipartFile mockMultipartFile2 = new MockMultipartFile(fieldName2, originalFilename2, contentType2, fileInputStream2);

        mockMvc.perform(multipart("/images/multiple")
                .file(mockMultipartFile1)
                .file(mockMultipartFile2)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validation.files").value(MultipartFileMultipleValid.FILENAME_EMPTY_MESSAGE));
    }

    @Test
    @DisplayName("이미지 멀티 전송시 이미지가 아닌 파일이 포함된 경우")
    void validWrongContentTypeMultipleImageUpload() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:static");
        File file1 = new File(resource.getFile().getAbsolutePath() + "/file/imageSample.jpg");
        String fieldName1 = "files";
        String originalFilename1 = "rybvy.png";
        String contentType1 = "image/png";
        FileInputStream fileInputStream1 = new FileInputStream(file1);
        MockMultipartFile mockMultipartFile1 = new MockMultipartFile(fieldName1, originalFilename1, contentType1, fileInputStream1);

        File file2 = new File(resource.getFile().getAbsolutePath() + "/file/excelSample.xlsx");
        String fieldName2 = "files";
        String originalFilename2 = "qwe.xlsx";
        String contentType2 = "application/vnd.ms-excel";
        FileInputStream fileInputStream2 = new FileInputStream(file2);
        MockMultipartFile mockMultipartFile2 = new MockMultipartFile(fieldName2, originalFilename2, contentType2, fileInputStream2);

        mockMvc.perform(multipart("/images/multiple")
                .file(mockMultipartFile1)
                .file(mockMultipartFile2)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validation.files").value(MultipartFileMultipleValid.MESSAGE));
    }

    @Test
    @DisplayName("이미지 멀티 전송 성공")
    void successMultipleImageUpload() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:static");
        File file1 = new File(resource.getFile().getAbsolutePath() + "/file/imageSample.jpg");
        String fieldName1 = "files";
        String originalFilename1 = "rybvy.png";
        String contentType1 = "image/png";
        FileInputStream fileInputStream1 = new FileInputStream(file1);
        MockMultipartFile mockMultipartFile1 = new MockMultipartFile(fieldName1, originalFilename1, contentType1, fileInputStream1);

        File file2 = new File(resource.getFile().getAbsolutePath() + "/file/imageSample.jpg");
        String fieldName2 = "files";
        String originalFilename2 = "rybvy2.png";
        String contentType2 = "image/png";
        FileInputStream fileInputStream2 = new FileInputStream(file2);
        MockMultipartFile mockMultipartFile2 = new MockMultipartFile(fieldName2, originalFilename2, contentType2, fileInputStream2);

        mockMvc.perform(multipart("/images/multiple")
                .file(mockMultipartFile1)
                .file(mockMultipartFile2)
            )
            .andExpect(status().isOk());

        List<Image> images = imageRepository.findAll();
        Set<String> originalFilenames = Set.of(originalFilename1, originalFilename2);
        boolean existsFiles = images.stream()
            .allMatch(image -> {
                try {
                    String filePath = resource.getFile().getAbsolutePath() + File.separator + ImageService.IMAGE_DIR
                        + File.separator + image.getSaveFilename();
                    File file = new File(filePath);
                    return file.exists();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        assertThat(imageRepository.count()).isEqualTo(2);
        assertThat(images.stream().allMatch(image -> originalFilenames.contains(image.getOriginalFilename()))).isTrue();
        assertThat(existsFiles).isTrue();
    }
}