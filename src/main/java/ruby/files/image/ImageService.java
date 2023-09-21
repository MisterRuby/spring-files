package ruby.files.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.common.file.FileInfo;
import ruby.files.common.file.FileService;
import ruby.files.common.file.MultipartFileCheck;
import ruby.files.image.exception.NotFoundFileException;

import java.util.List;

import static ruby.files.common.file.MultipartFileType.IMAGE;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final FileService fileService;
    private final static String IMAGE_DIR = "image";

    @MultipartFileCheck(checkType = IMAGE)
    public void upload(MultipartFile imageFile){
        FileInfo fileInfo = fileService.upload(imageFile, IMAGE_DIR);

        Image image = Image.builder()
            .originalFilename(fileInfo.getOriginalFilename())
            .saveFilename(fileInfo.getSaveFilename())
            .filePath(fileInfo.getFilePath())
            .size(fileInfo.getSize())
            .build();
        imageRepository.save(image);
    }

    @MultipartFileCheck(checkType = IMAGE)
    public void uploadMultiple(List<MultipartFile> imageFiles){
        imageFiles.forEach(this::upload);
    }

    public void delete(Long id) {
        Image image = imageRepository.findById(id)
            .orElseThrow(NotFoundFileException::new);

        fileService.deleteFile(image.getSaveFilename(), IMAGE_DIR);

        imageRepository.delete(image);
    }
}
