package ruby.files.excel.enums;

import lombok.Getter;

@Getter
public enum AddressUseCode {
    USE("Y"), UNUSED("N");

    private String code;

    AddressUseCode(String code) {
        this.code = code;
    }
}
