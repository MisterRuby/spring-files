package ruby.files.common.file.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ruby.files.common.file.FileInfo;
import ruby.files.common.file.FileService;
import ruby.files.common.file.exception.FailDownloadFileException;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3FileService implements FileService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public FileInfo upload(MultipartFile multipartFile, String parentDir) {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
        String saveFilename = UUID.randomUUID() + extension;
        String filePath = amazonS3Client.getUrl(bucket + File.separator + parentDir, saveFilename).toString();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        try {
            amazonS3Client.putObject(bucket, saveFilename, multipartFile.getInputStream(), metadata);

            return FileInfo.builder()
                .originalFilename(originalFilename)
                .saveFilename(saveFilename)
                .filePath(filePath)
                .size(multipartFile.getSize())
                .build();
        } catch (IOException e) {
            throw new FailDownloadFileException();
        }
    }

    @Override
    public ResponseEntity<Resource> download(String originalFilename, String filepath) {
        return null;
    }

    @Override
    public boolean deleteFile(String filePath) {
        return false;
    }
}
