package ruby.files.common.valid.multipart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Set;

@Getter
@AllArgsConstructor
public enum MultipartFileType {
    IMAGE(Set.of("image/png", "image/jpeg", "image/gif", "image/bmp", "image/webp")),
    EXCEL(Set.of("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel")),
    PDF(Set.of("application/pdf"));

    private final Set<String> contentTypes;

    public boolean isValidType(MultipartFile multipartFile) {
        return this.contentTypes.contains(Objects.requireNonNull(multipartFile.getContentType()).toLowerCase());
    }
}
