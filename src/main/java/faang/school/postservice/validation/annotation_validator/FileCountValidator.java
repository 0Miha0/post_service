package faang.school.postservice.validation.annotation_validator;

import faang.school.postservice.validation.annotation.MaxFilesPerPost;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileCountValidator implements ConstraintValidator<MaxFilesPerPost, MultipartFile[]> {

    @Override
    public boolean isValid(MultipartFile[] files, ConstraintValidatorContext context) {
        if (files == null) {
            return true;
        }
        return files.length <= 10;
    }
}
