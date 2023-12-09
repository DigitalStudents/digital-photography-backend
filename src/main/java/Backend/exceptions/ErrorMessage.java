package Backend.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ErrorMessage{
    private String message;
    private String exception;

    public ErrorMessage(Exception e) {
        this.message = e.getMessage();
        this.exception = e.getClass().getSimpleName();
    }

}
