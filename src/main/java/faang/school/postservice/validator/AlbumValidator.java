package faang.school.postservice.validator;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlbumValidator {

    private final UserServiceClient userServiceClient;

    public void doesExistUser(Long id) {
        if(userServiceClient.getUser(id) == null){
            log.warn("There is no user with ID {} in the system", id);
            throw new DataValidationException("There is no user with such ID");
        } else {
            log.info("There is a user with such ID");
        }
    }

    public void doesAuthorUser(Long userId, Long authorId) {
        if(!Objects.equals(userId, authorId)){
            log.warn("This user not author");
            throw new DataValidationException("This user not author");
        } else {
            log.info("This user author");
        }
    }
}
