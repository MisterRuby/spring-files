package ruby.files.common;

import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
public enum MultipartFileType {
    IMAGE(Set.of("image/png", "image/jpeg")),
    EXCEL(Set.of("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")),
    PDF(Set.of("application/pdf"));

    final Set<String> contentTypes;

    public boolean isValidType(MultipartFile multipartFile) {
        return this.contentTypes.contains(Objects.requireNonNull(multipartFile.getContentType()).toLowerCase());
    }
}
