package exceptions;

public class MissingFieldsInRequestException extends Exception {
    public MissingFieldsInRequestException(String message) {
        super(message);
    }
}
