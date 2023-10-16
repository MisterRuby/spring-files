package ruby.files.common.file.s3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import ruby.files.common.file.FileInfo;
import ruby.files.common.file.exception.NotFoundFileException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {DefaultResourceLoader.class, S3FileService.class, S3Config.class})
class S3FileServiceTest {

    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    S3FileService s3FileService;

    MockMultipartFile getUploadMockMultipartFile() throws IOException {
        String contentType = "text/plain";
        String filePath = "src/test/resources/static/file/testFile.txt";
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        return new MockMultipartFile("saveFile", "saveFile.txt", contentType, fileInputStream);
    }

    /** upload & download test - start */
    @Test
    @DisplayName("파일 업로드 & 다운로드")
    void successUploadAndDownload() throws IOException {
        String parentDir = "test";
        MockMultipartFile mockMultipartFile = getUploadMockMultipartFile();

        FileInfo fileInfo = s3FileService.upload(mockMultipartFile, parentDir);
        ResponseEntity<Resource> downloadFile = s3FileService.download(fileInfo.getOriginalFilename(), fileInfo.getFilePath());

        assertThat(Objects.requireNonNull(downloadFile.getBody()).isFile()).isTrue();
        assertThat(fileInfo.getSaveFilename()).isEqualTo(downloadFile.getBody().getFile().getName());
    }
    /** upload & download test - end */


    /** delete test - start */
    @Test
    @DisplayName("존재하지 않는 경로의 파일 삭제")
    void failDeleteWrongFilePath() throws IOException {
        String parentDir = "test";
        MockMultipartFile mockMultipartFile = getUploadMockMultipartFile();
        FileInfo fileInfo = s3FileService.upload(mockMultipartFile, parentDir);

        assertThatThrownBy(() -> s3FileService.deleteFile(fileInfo.getSaveFilename(), "wrongParent"))
            .hasMessage(new NotFoundFileException().getMessage());
    }

    @Test
    @DisplayName("파일 삭제")
    void successDelete() throws IOException {
        String parentDir = "test";
        MockMultipartFile mockMultipartFile = getUploadMockMultipartFile();
        FileInfo fileInfo = s3FileService.upload(mockMultipartFile, parentDir);

        s3FileService.deleteFile(fileInfo.getSaveFilename(), parentDir);
    }
    /** delete test - end */
}