package faang.school.postservice.exception;

import faang.school.postservice.exception.custom.DataValidationException;
import faang.school.postservice.exception.custom.FileDownloadException;
import faang.school.postservice.exception.custom.FileUploadException;
import faang.school.postservice.exception.custom.MinioException;
import faang.school.postservice.exception.custom.StreamingFileError;
import faang.school.postservice.exception.custom.ZippingFileError;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler({DataValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataValidationException(DataValidationException ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBindException(BindException ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler(FileDownloadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFileDownloadException(FileDownloadException ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFileUploadException(FileUploadException ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler(MinioException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleMinioException(MinioException ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler(StreamingFileError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleStreamingFileError(StreamingFileError ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler(ZippingFileError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleZippingFileError(ZippingFileError ex) {
        return buildResponse(ex);
    }

    private ErrorResponse buildResponse(Exception ex) {
        log.error(ex.getClass().getName(), ex);
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(ex.getClass().getName())
                .message(Objects.requireNonNullElse(ex.getMessage(), "No message available"))
                .build();
    }
}
