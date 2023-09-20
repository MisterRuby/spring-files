package ruby.files.image;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.common.file.FileUtils;
import ruby.files.common.file.MultipartFileCheck;
import ruby.files.common.file.exception.FailUploadFileException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static ruby.files.common.file.MultipartFileType.IMAGE;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ResourceLoader resourceLoader;
    private final FileUtils fileUtils;
    private final String IMAGE_DIR_PATH = "image";

    @MultipartFileCheck(checkType = IMAGE)
    public void upload(MultipartFile imageFile){
        String originalFilename = imageFile.getOriginalFilename();
        String extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
        String saveFilename = UUID.randomUUID() + extension;
        Resource resource = resourceLoader.getResource("classpath:static");

        try {
            System.out.println(imageFile.getContentType());

            File file = new File(resource.getFile().getAbsolutePath() + File.separator + IMAGE_DIR_PATH + File.separator + saveFilename);
            fileUtils.transferTo(imageFile, file);

            Image image = Image.builder()
                .originalFilename(originalFilename)
                .saveFilename(saveFilename)
                .filePath(file.getAbsolutePath())
                .size(imageFile.getSize())
                .build();

            imageRepository.save(image);
        } catch (IOException e) {
            throw new FailUploadFileException();
        }
    }

    @MultipartFileCheck(checkType = IMAGE)
    public void uploadMultiple(List<MultipartFile> imageFiles){
        imageFiles.forEach(this::upload);
    }

    public void uploadS3(MultipartFile image) {}
}
