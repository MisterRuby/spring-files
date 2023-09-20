package ruby.files.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ruby.files.image.dto.ImageDTO;
import ruby.files.image.dto.ImageMultipleUploadDTO;
import ruby.files.image.dto.ImageUploadDTO;
import ruby.files.image.dto.ImagesDTO;
import ruby.files.image.exception.NotFoundFileException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @PostMapping
    public void upload(ImageUploadDTO imageFile) {
      log.info("file name : {}", imageFile.getFile().getOriginalFilename());
      log.info("file contentType : {}", imageFile.getFile().getContentType());

        imageService.upload(imageFile.getFile());
    }

    @PostMapping("/multiple")
    public void uploadMultiple(ImageMultipleUploadDTO imageFiles) {
        imageService.uploadMultiple(imageFiles.getFiles());
    }

    @GetMapping
    public ImagesDTO getImages() {
        return new ImagesDTO(imageRepository.findAll());
    }

    @GetMapping("{id}")
    public ImageDTO getImage(@PathVariable Long id) {
        return new ImageDTO(imageRepository.findById(id).orElseThrow(NotFoundFileException::new));
    }
}
