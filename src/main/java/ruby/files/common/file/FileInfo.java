package ruby.files.common.file;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileInfo {

    private String originalFilename;
    private String saveFilename;
    private String filePath;
    private Long size;

    @Builder
    public FileInfo(String originalFilename, String saveFilename, String filePath, Long size) {
        this.originalFilename = originalFilename;
        this.saveFilename = saveFilename;
        this.filePath = filePath;
        this.size = size;
    }
}
