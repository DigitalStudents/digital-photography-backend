package Backend.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException{
    private static final HttpStatus badrequest= HttpStatus.BAD_REQUEST;

    public BadRequestException(String message) {
        super(badrequest+" "+ message);
    }
}
