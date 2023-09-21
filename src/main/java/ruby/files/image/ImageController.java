package ruby.files.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ruby.files.common.file.FileService;
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
    private final FileService fileService;

    @GetMapping
    public ImagesDTO getImageInfos() {
        return new ImagesDTO(imageRepository.findAll());
    }

    @GetMapping("{id}")
    public ImageDTO getImageInfo(@PathVariable Long id) {
        return new ImageDTO(imageRepository.findById(id).orElseThrow(NotFoundFileException::new));
    }

    @PostMapping("/upload")
    public void upload(ImageUploadDTO imageFile) {
        imageService.upload(imageFile.getFile());
    }

    @PostMapping("/upload/multiple")
    public void uploadMultiple(ImageMultipleUploadDTO imageFiles) {
        imageService.uploadMultiple(imageFiles.getFiles());
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        Image image = imageRepository.findById(id).orElseThrow(NotFoundFileException::new);

        return fileService.download(image.getOriginalFilename(), image.getFilePath());
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        imageService.delete(id);
    }
}
