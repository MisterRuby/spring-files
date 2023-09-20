package ruby.files.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.common.MultipartFileCheck;

import java.util.List;

import static ruby.files.common.MultipartFileType.IMAGE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    @MultipartFileCheck(checkTypes = {IMAGE})
    public void upload(@RequestParam("image") MultipartFile imageFile) {
      log.info("file name : {}", imageFile.getOriginalFilename());
      log.info("file contentType : {}", imageFile.getContentType());

        imageService.upload(imageFile);
    }

    @GetMapping
    public List<Image> getList() {
        return imageService.getList();
    }
}
