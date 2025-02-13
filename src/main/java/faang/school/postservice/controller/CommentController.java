package faang.school.postservice.controller;

import faang.school.postservice.dto.comment.CommentDto;
import faang.school.postservice.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Comment"  , description = "Operations related to comments"  )
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Create a new comment")
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDto dto,
                                              @RequestHeader("x-user-id") Long authorId
                                              ) {
        commentService.createComment(dto, authorId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all comments by post id")
    @PostMapping("/posts/{postId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getAllCommentsByPost(postId));
    }

    @Operation(summary = "Update comment")
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<Void> updateComment(@RequestBody CommentDto dto,
                                              @PathVariable Long commentId,
                                              @RequestHeader("x-user-id") Long authorId
                                              ) {
        commentService.updateComment(dto, commentId, authorId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete comment")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @RequestHeader("x-user-id") Long userId
                                              ) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok().build();
    }
}
