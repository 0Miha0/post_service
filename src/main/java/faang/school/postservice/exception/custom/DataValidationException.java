package faang.school.postservice.exception.custom;

public class DataValidationException extends RuntimeException{
    public DataValidationException(String message) {
        super(message);
    }
}
