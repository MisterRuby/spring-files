package ruby.files.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.common.MultipartFileCheck;

import static ruby.files.common.MultipartFileType.IMAGE;

@Slf4j
@RestController
@RequestMapping("/images")
public class ImageController {

    @PostMapping
    @MultipartFileCheck(checkTypes = {IMAGE})
    public void upload(@RequestParam("image") MultipartFile image) {
      log.info("file name : {}", image.getOriginalFilename());
      log.info("file contentType : {}", image.getContentType());
    }
}
