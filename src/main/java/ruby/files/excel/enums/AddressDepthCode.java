package ruby.files.excel.enums;

import lombok.Getter;

@Getter
public enum AddressDepthCode {
    ONE("00000000", (byte) 1), TWO("00000", (byte) 2);

    final String suffix;
    final byte depth;

    AddressDepthCode(String suffix, byte depth) {
        this.suffix = suffix;
        this.depth = depth;
    }
}
