package ruby.files.common.file;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileUtils {

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
}
