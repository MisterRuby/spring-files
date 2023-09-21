package ruby.files.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.common.file.FileUtils;
import ruby.files.common.file.MultipartFileCheck;
import ruby.files.image.exception.NotFoundFileException;

import java.io.File;
import java.util.List;

import static ruby.files.common.file.MultipartFileType.IMAGE;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final FileUtils fileUtils;

    @MultipartFileCheck(checkType = IMAGE)
    public void upload(MultipartFile imageFile){
        String image_dir = "image";
        File file = fileUtils.transferTo(imageFile, image_dir);

        Image image = Image.builder()
            .originalFilename(imageFile.getOriginalFilename())
            .saveFilename(file.getName())
            .filePath(file.getAbsolutePath())
            .size(imageFile.getSize())
            .build();
        imageRepository.save(image);
    }

    @MultipartFileCheck(checkType = IMAGE)
    public void uploadMultiple(List<MultipartFile> imageFiles){
        imageFiles.forEach(this::upload);
    }

    public void uploadS3(MultipartFile image) {}

    public void delete(Long id) {
        Image image = imageRepository.findById(id)
            .orElseThrow(NotFoundFileException::new);

        fileUtils.deleteFile(image.getFilePath());

        imageRepository.delete(image);
    }
}
