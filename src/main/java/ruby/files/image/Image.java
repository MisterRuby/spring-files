package ruby.files.image;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter

public class Image {

    @Id
    @GeneratedValue
    private Long id;
    private String originalFilename;
    private String saveFilename;
    private Long size;

    @Builder
    public Image(String originalFilename, String saveFilename, Long size) {
        this.originalFilename = originalFilename;
        this.saveFilename = saveFilename;
        this.size = size;
    }
}
