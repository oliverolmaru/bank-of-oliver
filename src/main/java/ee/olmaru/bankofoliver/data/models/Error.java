package ee.olmaru.bankofoliver.data.models;

import ee.olmaru.bankofoliver.data.models.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error {
    private ErrorCode code;
    private String message;

    public Error(ErrorCode code, String message) {
        this.code = code;
        this.message = message;
    }
}
