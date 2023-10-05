package ruby.files.common.file;

import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
public enum FileType {
    IMAGE(Set.of("image/png", "image/jpeg")),
    EXCEL(Set.of("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel")),
    PDF(Set.of("application/pdf"));

    final Set<String> contentTypes;

    public boolean isValidType(MultipartFile multipartFile) {
        return this.contentTypes.contains(Objects.requireNonNull(multipartFile.getContentType()).toLowerCase());
    }
}
