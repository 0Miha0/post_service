package faang.school.postservice.service.like;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.dto.like.LikeDto;
import faang.school.postservice.mapper.like.LikeMapper;
import faang.school.postservice.mapper.like.LikeMapperImpl;
import faang.school.postservice.model.Comment;
import faang.school.postservice.model.Like;
import faang.school.postservice.model.Post;
import faang.school.postservice.repository.LikeRepository;
import faang.school.postservice.service.comment.CommentService;
import faang.school.postservice.service.post.PostService;
import faang.school.postservice.validator.like.LikeValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private PostService postService;

    @Mock
    private CommentService commentService;

    @Mock
    private LikeValidator likeValidator;

    @Spy
    private LikeMapper likeMapper = new LikeMapperImpl();

    @InjectMocks
    private LikeService likeService;

    @Test
    public void addLikeToPostTest() {
        ArgumentCaptor<Like> captor = ArgumentCaptor.forClass(Like.class);
        long likeId = 1L;
        long userId = 1L;
        Long postId = 1L;

        LikeDto likeDto = LikeDto.builder()
                .id(likeId)
                .postId(postId)
                .userId(userId)
                .build();
        List<Like> likesOfPost = new ArrayList<>();
        Post postOfLike = Post.builder()
                .id(postId)
                .likes(likesOfPost)
                .build();

        when(likeRepository.findByPostId(postId)).thenReturn(new ArrayList<>());
        Like likeToCheckComment = new Like();
        when(likeRepository.findById(likeId)).thenReturn(Optional.of(likeToCheckComment));
        when(postService.getPost(postId)).thenReturn(postOfLike);

        likeService.addLikeToPost(likeDto);

        verify(userServiceClient).getUser(userId);
        verify(likeValidator).validateLikeHasTarget(postId, null);
        verify(likeRepository).findByPostId(postId);
        verify(likeValidator).validateUserAddOnlyOneLikeToPost(likesOfPost, userId);
        verify(likeRepository).findById(likeId);
        verify(likeValidator).validateLikeWasNotPutToComment(likeDto, likeToCheckComment);
        verify(likeMapper).toEntity(likeDto);

        verify(likeRepository).save(captor.capture());
        Like likeToSave = captor.getValue();
        verify(postService).addLikeToPost(postId, likeToSave);

        assertEquals(postOfLike, likeToSave.getPost());
        assertEquals(userId, likeToSave.getUserId());
    }

    @Test
    public void addLikeToCommentTest() {
        ArgumentCaptor<Like> captor = ArgumentCaptor.forClass(Like.class);
        long likeId = 1L;
        long userId = 1L;
        Long commentId = 1L;

        LikeDto likeDto = LikeDto.builder()
                .id(likeId)
                .commentId(commentId)
                .userId(userId)
                .build();
        List<Like> likesOfComment = new ArrayList<>();
        Comment commentOfLike = Comment.builder()
                .id(commentId)
                .likes(likesOfComment)
                .build();

        when(likeRepository.findByCommentId(commentId)).thenReturn(new ArrayList<>());
        Like likeToCheckPost = new Like();
        when(likeRepository.findById(likeId)).thenReturn(Optional.of(likeToCheckPost));
        when(commentService.getExistingComment(commentId)).thenReturn(commentOfLike);

        likeService.addLikeToComment(likeDto);

        verify(userServiceClient).getUser(userId);
        verify(likeValidator).validateLikeHasTarget(commentId, null);
        verify(likeRepository).findByCommentId(commentId);
        verify(likeValidator).validateUserAddOnlyOneLikeToComment(likesOfComment, userId);
        verify(likeRepository).findById(likeId);
        verify(likeValidator).validateLikeWasNotPutToPost(likeDto, likeToCheckPost);
        verify(likeMapper).toEntity(likeDto);

        verify(likeRepository).save(captor.capture());
        Like likeToSave = captor.getValue();
        verify(commentService).addLikeToComment(commentId, likeToSave);

        assertEquals(commentOfLike, likeToSave.getComment());
        assertEquals(userId, likeToSave.getUserId());
    }

    @Test
    public void removeLikeFromPostTest() {
        long likeID = 1L;
        long userId = 1L;
        long postId = 1L;
        LikeDto likeDto = LikeDto.builder()
                .id(likeID)
                .userId(userId)
                .postId(postId)
                .build();

        Like likeToRemove = Like.builder()
                .userId(userId)
                .build();

        when(likeRepository.findById(userId)).thenReturn(Optional.of(likeToRemove));
        doNothing().when(likeValidator).validateThisUserAddThisLike(userId, likeToRemove);

        likeService.removeLikeFromPost(likeID, likeDto);

        verify(likeRepository).findById(userId);
        verify(likeValidator).validateThisUserAddThisLike(userId, likeToRemove);
        verify(likeRepository).delete(likeToRemove);
        verify(postService).removeLikeFromPost(postId, likeToRemove);
    }

    @Test
    public void removeLikeFromCommentTest() {
        long likeID = 1L;
        long userId = 1L;
        long commentId = 1L;
        LikeDto likeDto = LikeDto.builder()
                .id(likeID)
                .userId(userId)
                .commentId(commentId)
                .build();

        Like likeToRemove = Like.builder()
                .userId(userId)
                .build();

        when(likeRepository.findById(userId)).thenReturn(Optional.of(likeToRemove));
        doNothing().when(likeValidator).validateThisUserAddThisLike(userId, likeToRemove);

        likeService.removeLikeFromComment(likeID, likeDto);

        verify(likeRepository).findById(userId);
        verify(likeValidator).validateThisUserAddThisLike(userId, likeToRemove);
        verify(likeRepository).delete(likeToRemove);
        verify(commentService).removeLikeFromComment(commentId, likeToRemove);
    }
}