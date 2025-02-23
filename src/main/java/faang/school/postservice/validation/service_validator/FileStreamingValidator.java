package faang.school.postservice.validation.service_validator;

import faang.school.postservice.exception.custom.DataValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;

@Slf4j
@Component
public class FileStreamingValidator {

    public void validateFileStreaming(InputStream file, String message) {
        if (file == null) {
            log.warn(message);
            throw new DataValidationException(message);
        } else {
            log.info("File streaming is valid");
        }
    }

    public void validateFilesStreaming(Map<String, InputStream> files, String message) {
        if (files == null || files.isEmpty()) {
            log.warn(message);
            throw new DataValidationException(message);
        } else {
            log.info("Files streaming is valid");
        }
    }
}
