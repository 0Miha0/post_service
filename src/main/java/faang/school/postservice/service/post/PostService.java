package faang.school.postservice.service.post;

import faang.school.postservice.dto.post.PostDto;
import faang.school.postservice.event_drive.redis.event.PostViewEvent;
import faang.school.postservice.event_drive.redis.event.PublishPostEvent;
import faang.school.postservice.event_drive.redis.publisher.PostViewPublisher;
import faang.school.postservice.event_drive.redis.publisher.PublishPostPublisher;
import faang.school.postservice.exception.custom.DataValidationException;
import faang.school.postservice.mapper.PostMapper;
import faang.school.postservice.model.Like;
import faang.school.postservice.model.Post;
import faang.school.postservice.repository.PostRepository;
import faang.school.postservice.validation.service_validator.PostValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostValidator postValidator;
    private final PostMapper postMapper;
    private final PostCorrector postCorrector;
    private final PostViewPublisher postViewPublisher;
    private final PublishPostPublisher publishPostPublisher;

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

        postViewPublisher.publish(
                PostViewEvent.builder()
                        .postId(postId)
                        .actorId(post.getAuthorId())
                        .receivedAt(LocalDateTime.now())
                        .build());

        publishPostPublisher.publish(
                PublishPostEvent.builder()
                        .userId(post.getAuthorId())
                        .build()
        );
        log.info("Published post {}", postId);
    }

    @Transactional
    public void updatePost(Long postId, PostDto dto) {
        log.info("Updating post {} with content: {}, ad: {}, resources: {}", postId, dto.getContent());
        Post post = findById(postId);
        if (dto.getContent() != null) {
            post.setContent(dto.getContent());
        }
        save(post);
        log.info("Updated post {}", postId);
    }

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

    public void publishScheduledPosts(int butchSize) {
        log.info("Publishing scheduled posts in batches of size {} in the stream {}", butchSize, Thread.currentThread().getName());
        List<Post> posts = postRepository.findExceedingPosts();
        List<List<Post>> batches = splitList(posts, butchSize);

        List<CompletableFuture<Void>> futures = batches.stream()
                .map(this::publishBatchAsync)
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.info("Published scheduled posts in batches in the stream {}", Thread.currentThread().getName());
    }

    @Async("scheduledPostPublish")
    @Transactional
    public CompletableFuture<Void> publishBatchAsync(List<Post> batch) {
        log.info("Publishing a party of posts ({} posts) in the stream {}", batch.size(), Thread.currentThread().getName());
        return CompletableFuture.runAsync(() -> {
            log.info("Processing batch of {} posts", batch.size());
            batch.forEach(post -> {
                post.setPublished(true);
                post.setPublishedAt(LocalDateTime.now());
                log.info("Published post with ID: {}", post.getId());
            });
            log.info("Persisted batch of {} posts", batch.size());
            saveAll(batch);
            log.info("Published batch of {} posts", batch.size());
        });
    }

    public Mono<String> correctPostContent(Long postId) {
        log.info("Correcting content of post with id: {}", postId);
        return postCorrector.correctPost(findById(postId).getContent());
    }

    public List<Post> saveAll(List<Post> posts) {
        log.info("Persisting {} posts", posts.size());
        return postRepository.saveAll(posts);
    }

    public void save(Post post) {
        log.info("Saving post: {}", post.getId());
        postRepository.save(post);
        log.info("Saved post with ID: {}", post.getId());
    }

    public Post findById(Long id) {
        log.info("Finding post by id: {}", id);
        return postRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Post not found with id: {}", id);
                    return new EntityNotFoundException("Post not found with id: " + id);
                });
    }

    private List<List<Post>> splitList(List<Post> posts, int batchSize) {
        log.info("Splitting list of {} posts into batches of size {}", posts.size(), batchSize);
        return List.copyOf(posts)
                .stream()
                .collect(Collectors.groupingBy(it -> posts.indexOf(it) / batchSize))
                .values()
                .stream()
                .toList();
    }
}
