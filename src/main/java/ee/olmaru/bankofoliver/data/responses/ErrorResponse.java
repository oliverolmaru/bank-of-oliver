package ee.olmaru.bankofoliver.data.responses;

import ee.olmaru.bankofoliver.data.models.Error;
import ee.olmaru.bankofoliver.data.models.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ErrorResponse {
    public Error error;

    public ErrorResponse(Error error) {
        this.error = error;
    }

    public ErrorResponse(ErrorCode errorCode, String errorMessage){
        this.error = new Error(errorCode, errorMessage);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error=" + error +
                '}';
    }
}


