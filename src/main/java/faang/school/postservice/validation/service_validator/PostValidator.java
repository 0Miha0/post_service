package faang.school.postservice.validation.service_validator;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.exception.custom.DataValidationException;
import faang.school.postservice.exception.custom.FileUploadException;
import faang.school.postservice.model.Post;
import faang.school.postservice.model.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostValidator {

    private final UserServiceClient userServiceClient;

    public void doesExistUser(Long id) {
        if(userServiceClient.getUser(id) == null){
            log.warn("There is no user with ID {} in the system", id);
            throw new DataValidationException("There is no user with such ID");
        } else {
            log.info("There is a user with such ID");
        }
    }

    public void isUserTheAuthor(Long userId, Long authorId) {
        if(Objects.equals(userId, authorId)) {
            log.info("This user with author");
        } else {
            log.warn("User with id: {} not author post", authorId);
            throw new DataValidationException("User with id: " + authorId + " not author post");
        }
    }

    public boolean doTheFilesExist(MultipartFile[] files) {
        log.info("Checking if files exist");
        return files != null && files.length != 0;
    }

    public void validateResourceExists(Resource resource, Post post) {
        if (resource == null || !post.getResources().contains(resource)) {
            log.warn("Resource with id {} not found in post {}", resource.getId(), post.getId());
            throw new DataValidationException("Resource not found in the specified post");
        }
    }

    public boolean doesTheSizeMatch(int width, int height) {
        if ((width > 1080 && height <= 566) || (width <= 1080 && height > 1080) || (width > 1080 && height > 566)) {
            log.warn("Resource size {}x{} does not match the required size (1080x566)", width, height);
            return true;
        } else {
            log.info("Resource size {}x{} matches the required size (1080x566)", width, height);
            return false;
        }
    }

    public void nonvalizesLimiter(Long fileSize) {
        if (fileSize > 5 * 1024 * 1024) {
            log.warn("File size: {} exceeds the maximum allowed limit of 5MB.", fileSize);
            throw new FileUploadException("File size exceeds the maximum allowed limit of 5MB.");
        }
    }
}
