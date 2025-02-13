package faang.school.postservice.service;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.dto.post.PostDto;
import faang.school.postservice.exception.DataValidationException;
import faang.school.postservice.mapper.PostMapper;
import faang.school.postservice.model.Like;
import faang.school.postservice.model.Post;
import faang.school.postservice.repository.PostRepository;
import faang.school.postservice.validator.PostValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostValidator postValidator;
    private final PostMapper postMapper;

    public void createPost(PostDto dto, Long authorId) {
        log.info("Creating post for user {}", authorId);
        postValidator.doesExistUser(authorId);

        Post post = postMapper.toEntity(dto);
        post.setAuthorId(authorId);
        post.setPublished(false);
        post.setDeleted(false);

        save(post);

        log.info("Created post with ID: {}", post.getId());
    }

    public void publishPost(Long postId) {
        log.info("Publishing post {}", postId);
        Post post = findById(postId);
        if (post.isPublished()) {
            log.warn("This post has already been published");
            throw new DataValidationException("This post has already been published");
        } else {
            log.info("Marking post {} as published", postId);
            post.setPublished(true);
            post.setPublishedAt(LocalDateTime.now());
            save(post);
        }
        log.info("Published post {}", postId);
    }

    public void updatePost(Long postId, PostDto dto) {
       log.info("Updating post {} with content: {}, ad: {}, resources: {}", postId, dto.getContent());
        Post post = findById(postId);
        if (dto.getContent() != null) {
            post.setContent(dto.getContent());
        }
        save(post);
        log.info("Updated post {}", postId);
    }

    @Transactional
    public void deletePost(Long postId) {
        log.info("Deleting post {}", postId);
        Post post = findById(postId);
        post.setDeleted(true);
        save(post);
        log.info("Deleted post {}", postId);
    }

    public PostDto getPostById(Long postId) {
        log.info("Getting post by id: {}", postId);
        Post post = findById(postId);
        return postMapper.toDto(post);
    }

    public List<PostDto> getAllDraftPostsByUserId(Long userId) {
        log.info("Getting all draft posts by user id: {}", userId);
        List<Post> posts = postRepository.findPublishedFalse(userId);
        posts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));
        return postMapper.toDtoList(posts);
    }

    public List<PostDto> getAllPublishedPostsByUserId(Long userId) {
        log.info("Getting all published posts by user id: {}", userId);
        List<Post> posts = postRepository.findPublishedTrue(userId);
        posts.sort((p1, p2) -> p2.getPublishedAt().compareTo(p1.getPublishedAt()));
        return postMapper.toDtoList(posts);
    }

    public void removeLikeFromPost(Long postId, Like like) {
        log.info("Removing like from post with id {} and user id {}", postId, like.getUserId());
        Post post = findById(postId);
        post.getLikes().remove(like);

        log.info("Removing like from post with id {}", postId);
        save(post);
        log.info("Removed like from post with id {}", postId);
    }

    public void save(Post post) {
        log.info("Saving post: {}", post.getId());
        postRepository.save(post);
        log.info("Saved post with ID: {}", post.getId());
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Post not found with id: {}", id);
                    return new EntityNotFoundException("Post not found with id: " + id);
                });
    }
}
