package Backend.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends RuntimeException{
    private static final  HttpStatus httpStatus= HttpStatus.CONFLICT;

    public ConflictException(String message) {
        super(httpStatus+". "+message);
    }
}
