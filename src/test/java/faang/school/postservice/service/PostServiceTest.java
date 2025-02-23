package faang.school.postservice.service;

import faang.school.postservice.dto.post.PostDto;
import faang.school.postservice.exception.custom.DataValidationException;
import faang.school.postservice.mapper.PostMapperImpl;
import faang.school.postservice.model.Post;
import faang.school.postservice.repository.PostRepository;
import faang.school.postservice.service.post.PostService;
import faang.school.postservice.validation.service_validator.PostValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostValidator postValidator;

    @Spy
    private PostMapperImpl postMapper;

    @InjectMocks
    private PostService postService;

    private PostDto postDto;
    private Post post;
    private Long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;

        post = Post.builder()
                .content("Test content")
                .authorId(userId)
                .published(false)
                .build();

        postDto = PostDto.builder()
                .content("Test content")
                .build();
    }

//    @Test
//    void successfullyCreatePostTest() {
//        doNothing().when(postValidator).doesExistUser(userId);
//
//        postService.createPost(postDto, userId);
//
//        verify(postRepository, times(1)).save(post);
//    }
//
//    @Test
//    void successfullyPublishPostTest() {
//        Long postId = 1L;
//
//        when(postRepository.findById(postId))
//                .thenReturn(Optional.ofNullable(post));
//
//        postService.publishPost(postId);
//
//        verify(postRepository, times(1)).save(post);
//    }
//
//    @Test
//    void failedPublishPostTest() {
//        Long postId = 1L;
//
//        Post unPublishPost = Post.builder()
//                .content("Test content")
//                .authorId(userId)
//                .published(true)
//                .build();
//
//        when(postRepository.findById(postId))
//                .thenReturn(Optional.ofNullable(unPublishPost));
//
//
//        DataValidationException exception = assertThrows(DataValidationException.class, () -> {
//            postService.publishPost(postId);
//        });
//
//        assertEquals("This post has already been published", exception.getMessage());
//    }
//

}
