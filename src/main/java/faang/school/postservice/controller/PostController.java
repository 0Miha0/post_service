package faang.school.postservice.controller;

import faang.school.postservice.dto.post.PostDto;
import faang.school.postservice.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Post", description = "Operations related to posts"  )
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "Create a new post")
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostDto postDto,
                                           @RequestHeader("x-user-id") Long authorId
                                           ) {
        postService.createPost(postDto, authorId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Publish post")
    @PutMapping("/{postId}/publish")
    public ResponseEntity<Void> publishPost(@PathVariable Long postId) {
        postService.publishPost(postId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "UpdatePost")
    @PutMapping("/{postId}/update")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId,
                                           @RequestBody PostDto dto
                                           ) {
        postService.updatePost(postId, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete post")
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get post by id")
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @Operation(summary = "Get all draft posts by user id")
    @GetMapping("/drafts/{userId}")
    public ResponseEntity<List<PostDto>> getAllDraftPostsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getAllDraftPostsByUserId(userId));
    }

    @Operation(summary = "Get all published posts by user id")
    @GetMapping("/published/{userId}")
    public ResponseEntity<List<PostDto>> getAllPublishedPostsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getAllPublishedPostsByUserId(userId));
    }
}
