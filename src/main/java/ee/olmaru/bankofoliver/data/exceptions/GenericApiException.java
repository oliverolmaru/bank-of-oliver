package ee.olmaru.bankofoliver.data.exceptions;

import ee.olmaru.bankofoliver.data.models.Error;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericApiException extends RuntimeException{
    private Error error;

    public GenericApiException(Error error){
        super();
        this.error = error;
    }
}
