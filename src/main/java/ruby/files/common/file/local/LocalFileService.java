package ruby.files.common.file.local;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.common.file.FileInfo;
import ruby.files.common.file.FileService;
import ruby.files.common.file.exception.FailDownloadFileException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalFileService implements FileService {

    private final ResourceLoader resourceLoader;

    public FileInfo upload(MultipartFile multipartFile, String parentDir) {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
        String saveFilename = UUID.randomUUID() + extension;

        Resource resource = resourceLoader.getResource("classpath:static");

        try {
            String filePath = resource.getFile().getAbsolutePath() + File.separator + parentDir + File.separator + saveFilename;
            File file = new File(filePath);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            multipartFile.transferTo(file);

            return FileInfo.builder()
                .originalFilename(originalFilename)
                .saveFilename(saveFilename)
                .filePath(file.getAbsolutePath())
                .size(multipartFile.getSize())
                .build();
        } catch (IOException e) {
            throw new FailDownloadFileException();
        }
    }

    @Override
    public ResponseEntity<Resource> download(String originalFilename, String filepath) {
        try {
            UrlResource resource = new UrlResource("file:" + filepath);
            String contentDisposition = getContentDisposition(originalFilename);

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
        } catch (MalformedURLException e) {
            throw new FailDownloadFileException();
        }
    }

    @Override
    public void deleteFile(String saveFilename, String parentDir) {
        Resource resource = resourceLoader.getResource("classpath:static");
        try {
            String filePath = resource.getFile().getAbsolutePath() + File.separator + parentDir + File.separator + saveFilename;
            File file = new File(filePath);
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
