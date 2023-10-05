package ruby.files.excel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Address {

    @Id
    private String bcode;
    private String addressName;
    private byte depth;

    @Builder
    public Address(String bcode, String addressName, byte depth) {
        this.bcode = bcode;
        this.addressName = addressName;
        this.depth = depth;
    }
}
