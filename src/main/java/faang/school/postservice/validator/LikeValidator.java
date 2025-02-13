package faang.school.postservice.validator;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeValidator {

    private final UserServiceClient userServiceClient;

    public void doesExistUser(Long id) {
        if(userServiceClient.getUser(id) == null){
            throw new DataValidationException("There is no user with such ID");
        } else {
            log.info("There is a user with such ID");
        }
    }
}
