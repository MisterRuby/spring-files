package ruby.files.common.file;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileInfo upload(MultipartFile imageFile, String imageDir);

    ResponseEntity<Resource> download(String originalFilename, String filepath);

    boolean deleteFile(String filePath);
}
