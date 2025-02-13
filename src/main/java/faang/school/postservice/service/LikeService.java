package faang.school.postservice.service;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.dto.user.UserDto;
import faang.school.postservice.model.Like;
import faang.school.postservice.repository.LikeRepository;
import faang.school.postservice.validator.LikeValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserServiceClient userServiceClient;
    private final LikeRepository likeRepository;
    private final LikeValidator likeValidator;
    private final PostService postService;
    private final CommentService commentService;

    public void likePost(Long postId, Long userId) {
        log.info("Liking post {}", postId);
        likeValidator.doesExistUser(userId);
        Like like = Like.builder()
                .userId(userId)
                .post(postService.findById(postId))
                .createdAt(LocalDateTime.now())
                .build();
        save(like);
        log.info("Liked post {}", postId);
    }

    public void likeComment(Long commentId, Long userId) {
        log.info("Liking comment {}", commentId);
        likeValidator.doesExistUser(userId);
        Like like = Like.builder()
                .userId(userId)
                .comment(commentService.findById(commentId))
                .createdAt(LocalDateTime.now())
                .build();
        save(like);
        log.info("Liked comment {}", commentId);
    }

    public void deleteLikeComment(Long commentId, Long likeId) {
        log.info("Deleting like for comment {}", commentId);
        Like like = findById(likeId);
        deleteById(commentId);
        commentService.removeLikeFromComment(commentId, like);
        log.info("Deleted like for comment {}", commentId);
    }

    public void deleteLikePost(Long postId, Long likeId) {
        log.info("Deleting like for post {} by user {}", postId, likeId);
        Like like = findById(likeId);
        deleteById(likeId);
        postService.removeLikeFromPost(postId, like);
        log.info("Deleted like for post {} by user {}", postId, likeId);
    }

    public List<UserDto> getLikeUsersPost(Long postId) {
        log.info("Getting like users for post {}", postId);
        return userServiceClient.getUsersByIds(
                postService.findById(postId).getLikes().stream()
                        .map(Like::getUserId)
                        .toList());
    }

    public List<UserDto> getLikeUsersComment(Long commentId) {
        log.info("Getting like users for comment {}", commentId);
        return userServiceClient.getUsersByIds(
                commentService.findById(commentId).getLikes().stream()
                       .map(Like::getUserId)
                       .toList());
    }

    public Like findById(Long id) {
        log.info("Finding like by id: {}", id);
        return likeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Like not found with id: {}", id);
                    return new EntityNotFoundException("Like not found with id: " + id);
                });
    }

    public void save(Like like) {
        log.info("Saving like: {}", like.getId());
        likeRepository.save(like);
        log.info("Saved like with ID: {}", like.getId());
    }

    public void deleteById(Long id) {
        log.info("Deleting like with id: {}", id);
        likeRepository.deleteById(id);
        log.info("Deleted like with id: {}", id);
    }
}
