package faang.school.postservice.validation.service_validator;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.exception.custom.DataValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentValidator {

    private final UserServiceClient userServiceClient;

    public void doesExistUser(Long userId) {
        if(userServiceClient.getUser(userId) == null){
            log.warn("User with ID {} does not exist", userId);
            throw new DataValidationException("There is no user with such ID");
        } else {
            log.info("User with ID {} exists", userId);
        }
    }

    public void authorizedUser(Long userId , Long authorId) {
        if(Objects.equals(userId, authorId)){
            log.info("This user author");
        } else {
            log.warn("This user not author");
            throw new DataValidationException("This user not author");
        }
    }
}
