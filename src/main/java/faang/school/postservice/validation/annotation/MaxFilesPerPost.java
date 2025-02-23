package faang.school.postservice.validation.annotation;

import faang.school.postservice.validation.annotation_validator.FileCountValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FileCountValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxFilesPerPost {
    String message() default "You cannot upload more than 10 files.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}