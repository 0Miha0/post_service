package faang.school.postservice.controller;

import faang.school.postservice.dto.user.UserDto;
import faang.school.postservice.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Like")
@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "Like a post")
    @PostMapping("/post/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId,
                                         @RequestHeader("x-user-id") Long userId
                                         ) {
        likeService.likePost(postId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Like a comment")
    @PostMapping("/comment/{commentId}/")
    public ResponseEntity<Void> likeComment(@PathVariable Long commentId,
                                            @RequestHeader("x-user-id") Long userId
                                            ) {
        likeService.likeComment(commentId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a like from a comment")
    @DeleteMapping("/comment/{commentId}/{likeId}")
    public ResponseEntity<Void> deleteLikeComment(@PathVariable Long commentId,
                                                  @PathVariable Long likeId
                                                  ) {
        likeService.deleteLikeComment(commentId, likeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a like from a post")
    @DeleteMapping("/post/{postId}/{likeId}")
    public ResponseEntity<Void> deleteLikePost(@PathVariable Long postId,
                                               @PathVariable Long likeId
                                               ) {
        likeService.deleteLikePost(postId, likeId);
        return ResponseEntity.ok().build();
    }

    @Operation
    @GetMapping("/{postId}")
    public ResponseEntity<List<UserDto>> getLikeUsersPost(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.getLikeUsersPost(postId));
    }

    @Operation
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<List<UserDto>> getLikeUsersComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(likeService.getLikeUsersComment(commentId));
    }
}
