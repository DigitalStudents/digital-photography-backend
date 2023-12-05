package Backend.exceptions;

public class CaracteristicaNotFoundException extends RuntimeException {
    public CaracteristicaNotFoundException(String message) {
        super(message);
    }
}