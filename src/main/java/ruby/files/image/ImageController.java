package ruby.files.image;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ruby.files.common.file.FileService;
import ruby.files.common.file.exception.NotFoundFileException;
import ruby.files.image.dto.ImageMultipleUploadDTO;
import ruby.files.image.dto.ImageUploadDTO;
import ruby.files.image.dto.ImagesDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final FileService fileService;

    @PostMapping
    public void upload(@Valid ImageUploadDTO imageFile) {
        imageService.upload(imageFile.getFile());
    };

    @PostMapping("/multiple")
    public void uploadMultiple(@Valid ImageMultipleUploadDTO imageFiles) {
        imageService.uploadMultiple(imageFiles.getFiles());
    }

    @GetMapping
    public ImagesDTO getImageInfos() {
        return new ImagesDTO(imageRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        Image image = imageRepository.findById(id).orElseThrow(NotFoundFileException::new);

        return fileService.download(image.getOriginalFilename(), image.getFilePath());
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        imageService.delete(id);
    }
}
