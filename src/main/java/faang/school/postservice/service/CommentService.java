package faang.school.postservice.service;

import faang.school.postservice.dto.comment.CommentDto;
import faang.school.postservice.mapper.CommentMapper;
import faang.school.postservice.model.Comment;
import faang.school.postservice.model.Like;
import faang.school.postservice.repository.CommentRepository;
import faang.school.postservice.validator.CommentValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final CommentMapper commentMapper;
    private final CommentValidator commentValidator;

    public void createComment(CommentDto dto, Long authorId) {
        log.info("Creating comment for user {}", authorId);
        commentValidator.doesExistUser(authorId);
        Comment comment = commentMapper.toEntity(dto);
        comment.setAuthorId(authorId);
        comment.setCreatedAt(LocalDateTime.now());
        save(comment);
        log.info("Created comment with ID: {}", comment.getId());
    }

    public List<CommentDto> getAllCommentsByPost(Long postId) {
        log.info("Getting all comments for post {}", postId);
        return postService.findById(postId).getComments().stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .map(commentMapper::toDto)
                .toList();
    }

    public void updateComment(CommentDto dto, Long commentId, Long authorId) {
        log.info("Updating comment {} with content: {}", commentId, dto.getContent());
        Comment comment = findById(commentId);
        commentValidator.authorizedUser(authorId, comment.getAuthorId());
        comment.setContent(dto.getContent());
        save(comment);
        log.info("Updated comment {}", commentId);
    }

    public void deleteComment(Long commentId, Long userId) {
        log.info("Deleting comment {}", commentId);
        commentValidator.authorizedUser(
                userId, findById(commentId).getAuthorId()
        );
        deleteById(commentId);
        log.info("Deleted comment {}", commentId);
    }

    public void removeLikeFromComment(Long commentId, Like like) {
        log.info("Removing like from comment with ID: {}", commentId);
        Comment comment = findById(commentId);
        comment.getLikes().remove(like);

        log.info("Removing like from comment with ID: {}", comment.getId());
        commentRepository.save(comment);
    }

    public void deleteById(Long commentId) {
        log.info("Deleting comment {}", commentId);
        commentRepository.deleteById(commentId);
        log.info("Deleted comment {}", commentId);
    }

    public void save(Comment comment) {
        log.info("Saving comment {}", comment.getId());
        commentRepository.save(comment);
        log.info("Saved comment {}", comment.getId());
    }

    public Comment findById(Long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Comment not found with id: {}", id);
                    return new EntityNotFoundException("Comment not found with id: " + id);
                });
    }
}
