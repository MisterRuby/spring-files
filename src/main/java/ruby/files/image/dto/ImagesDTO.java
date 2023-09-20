package ruby.files.image.dto;

import lombok.Getter;
import ruby.files.image.Image;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ImagesDTO {

    private List<ImageDTO> images;

    public ImagesDTO(List<Image> imageEntities) {
        this.images = imageEntities.stream()
            .map(ImageDTO::new)
            .collect(Collectors.toList());
    }
}
