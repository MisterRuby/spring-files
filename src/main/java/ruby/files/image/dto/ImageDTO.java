package ruby.files.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ruby.files.image.Image;

@Getter
@AllArgsConstructor
public class ImageDTO {

    private Long id;
    private String originalFilename;
    private String saveFilename;
    private String filePath;
    private Long size;

    public ImageDTO(Image image) {
        this.id = image.getId();
        this.originalFilename = image.getOriginalFilename();
        this.saveFilename = image.getSaveFilename();
        this.filePath = image.getFilePath();
        this.size = image.getSize();
    }
}
