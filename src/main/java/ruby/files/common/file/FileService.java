package ruby.files.common.file;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public interface FileService {

    FileInfo upload(MultipartFile imageFile, String imageDir);

    ResponseEntity<Resource> download(String originalFilename, String filepath);

    default String getContentDisposition(String originalFilename) {
        String filename = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8);
        return  "attachment; filename=\"" + filename + "\"";
    }

    void deleteFile(String saveFilename, String parentDir);
}
