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

@Component
@RequiredArgsConstructor
public class FileUtils {

    private final ResourceLoader resourceLoader;

    public void transferTo(MultipartFile imageFile, File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            imageFile.transferTo(file);
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

    public String getContentDisposition(String originalFilename) {
        String filename = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8);
        return  "attachment; filename=\"" + filename + "\"";
    }
}
