package ruby.files.common.file.local;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

@Slf4j
@SpringBootTest(classes = {LocalFileService.class})
class LocalFileServiceTest {

    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    LocalFileService localFileService;

    MockMultipartFile getUploadMockMultipartFile() throws IOException {
        String contentType = "text/plain";
        String filePath = "src/test/resources/static/file/testFile.txt";
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        return new MockMultipartFile("saveFile", "saveFile.txt", contentType, fileInputStream);
    }

    /** upload test - start */
    @Test
    @DisplayName("파일 업로드(저장)")
    void successUpload() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static");
        String parentDir = "test";
        String filePath = resource.getFile().getAbsolutePath() + File.separator + parentDir;
        File parentFile = new File(filePath);
        FileUtils.deleteDirectory(parentFile);

        MockMultipartFile mockMultipartFile = getUploadMockMultipartFile();
        localFileService.upload(mockMultipartFile, parentDir);

        parentFile = new File(filePath);
        assertThat(FileUtils.isEmptyDirectory(parentFile)).isFalse();
    }
    /** upload test - end */

    /** download test - start */
    @Test
    @DisplayName("존재하지 않는 경로의 파일 다운로드")
    void failDownloadByWrongFilePath() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static");
        String parentDir = "test";
        String filePath = resource.getFile().getAbsolutePath() + File.separator + parentDir;
        File parentFile = new File(filePath);
        FileUtils.deleteDirectory(parentFile);
        MockMultipartFile mockMultipartFile = getUploadMockMultipartFile();
        FileInfo fileInfo = localFileService.upload(mockMultipartFile, parentDir);

        ResponseEntity<Resource> downloadFile = localFileService.download(fileInfo.getOriginalFilename(), "wrongPath");
        assertThat(downloadFile.getBody().getFile().exists()).isFalse();
    }

    @Test
    @DisplayName("파일 다운로드")
    void successDownload() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static");
        String parentDir = "test";
        String filePath = resource.getFile().getAbsolutePath() + File.separator + parentDir;
        File parentFile = new File(filePath);
        FileUtils.deleteDirectory(parentFile);
        MockMultipartFile mockMultipartFile = getUploadMockMultipartFile();
        FileInfo fileInfo = localFileService.upload(mockMultipartFile, parentDir);

        ResponseEntity<Resource> downloadFile =
            localFileService.download(fileInfo.getOriginalFilename(), fileInfo.getFilePath());

        assertThat(Objects.requireNonNull(downloadFile.getBody()).isFile()).isTrue();
        assertThat(fileInfo.getSaveFilename()).isEqualTo(downloadFile.getBody().getFile().getName());
    }
    /** download test - end */

    /** delete test - start */
    @Test
    @DisplayName("존재하지 않는 경로의 파일 삭제")
    void failDeleteWrongFilePath() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static");
        String parentDir = "test";
        String filePath = resource.getFile().getAbsolutePath() + File.separator + parentDir;
        File parentFile = new File(filePath);
        FileUtils.deleteDirectory(parentFile);
        MockMultipartFile mockMultipartFile = getUploadMockMultipartFile();
        FileInfo fileInfo = localFileService.upload(mockMultipartFile, parentDir);

        assertThatThrownBy(() -> localFileService.deleteFile(fileInfo.getSaveFilename(), "wrongParent"))
            .hasMessage(new NotFoundFileException().getMessage());
    }

    @Test
    @DisplayName("파일 삭제")
    void successDelete() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static");
        String parentDir = "test";
        String filePath = resource.getFile().getAbsolutePath() + File.separator + parentDir;
        File parentFile = new File(filePath);
        FileUtils.deleteDirectory(parentFile);
        MockMultipartFile mockMultipartFile = getUploadMockMultipartFile();
        FileInfo fileInfo = localFileService.upload(mockMultipartFile, parentDir);

        localFileService.deleteFile(fileInfo.getSaveFilename(), parentDir);
    }
    /** delete test - end */
}