package ruby.files.common.file;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.common.file.exception.FailDownloadFileException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileUtils {

    private final ResourceLoader resourceLoader;

    public File transferTo(MultipartFile imageFile, String image_dir) {
        String originalFilename = imageFile.getOriginalFilename();
        String extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
        String saveFilename = UUID.randomUUID() + extension;

        Resource resource = resourceLoader.getResource("classpath:static");

        try {
            File file = new File(resource.getFile().getAbsolutePath() + File.separator + image_dir + File.separator + saveFilename);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            imageFile.transferTo(file);
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    private String getContentDisposition(String originalFilename) {
        String filename = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8);
        return  "attachment; filename=\"" + filename + "\"";
    }
}
