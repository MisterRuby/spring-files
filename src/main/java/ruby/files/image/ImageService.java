package ruby.files.image;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.common.MultipartFileCheck;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static ruby.files.common.MultipartFileType.IMAGE;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ResourceLoader resourceLoader;
    private final String IMAGE_DIR_PATH = "image";

    @MultipartFileCheck(checkTypes = {IMAGE})
    public void upload(MultipartFile imageFile){
        Resource resource = resourceLoader.getResource("classpath:static");
        String originalFilename = imageFile.getOriginalFilename();
        String extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
        String saveFilename = UUID.randomUUID() + extension;

        try {
            File file = new File(resource.getFile().getAbsolutePath() + File.separator + IMAGE_DIR_PATH + File.separator + saveFilename + extension);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            imageFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Image image = Image.builder()
            .originalFilename(originalFilename)
            .saveFilename(saveFilename)
            .size(imageFile.getSize())
            .build();

        imageRepository.save(image);
    }

    public void uploadS3(MultipartFile image) {}

    public List<Image> getList() {
        return imageRepository.findAll();
    }
}
