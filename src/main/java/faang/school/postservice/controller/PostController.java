package faang.school.postservice.controller;

import faang.school.postservice.dto.post.PostDto;
import faang.school.postservice.service.post.PostFileService;
import faang.school.postservice.service.post.PostService;
import faang.school.postservice.validation.annotation.MaxFilesPerPost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@Tag(name = "Post", description = "Operations related to posts")
@Slf4j
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostFileService postFileService;

    @Operation(summary = "Create a new post")
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody @NotNull PostDto postDto,
                                           @RequestHeader("x-user-id") @NotNull Long authorId) {
        log.info("Creating a new post by user {}", authorId);
        postService.createPost(postDto, authorId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Publish post")
    @PutMapping("/{postId}/publish")
    public ResponseEntity<Void> publishPost(@PathVariable @NotNull Long postId) {
        log.info("Publishing post {}", postId);
        postService.publishPost(postId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update post")
    @PutMapping("/{postId}/update")
    public ResponseEntity<Void> updatePost(@PathVariable @NotNull Long postId,
                                           @RequestBody @NotNull PostDto dto) {
        log.info("Updating post {}", postId);
        postService.updatePost(postId, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete post")
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable @NotNull Long postId) {
        log.info("Deleting post {}", postId);
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get post by id")
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable @NotNull Long postId) {
        log.info("Fetching post by id {}", postId);
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @Operation(summary = "Get all draft posts by user id")
    @GetMapping("/drafts/{userId}")
    public ResponseEntity<List<PostDto>> getAllDraftPostsByUserId(@PathVariable @NotNull Long userId) {
        log.info("Fetching all draft posts by user {}", userId);
        return ResponseEntity.ok(postService.getAllDraftPostsByUserId(userId));
    }

    @Operation(summary = "Get all published posts by user id")
    @GetMapping("/published/{userId}")
    public ResponseEntity<List<PostDto>> getAllPublishedPostsByUserId(@PathVariable @NotNull Long userId) {
        log.info("Fetching all published posts by user {}", userId);
        return ResponseEntity.ok(postService.getAllPublishedPostsByUserId(userId));
    }

    @Operation(summary = "Add file to post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file data or request"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred during processing")
    })
    @PostMapping("/{postId}/resources")
    public ResponseEntity<Void> addFileToPost(@PathVariable @NotNull Long postId,
                                              @RequestPart(value = "filesPerPost", required = false) @MaxFilesPerPost MultipartFile[] files,
                                              @RequestHeader("x-user-id") @NotNull Long userId) {
        log.info("Adding files to post {} by user {}", postId, userId);
        postFileService.addFileToPost(postId, files, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove file from post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File removed successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred during processing")
    })
    @DeleteMapping("/{postId}/resources/{resourceId}")
    public ResponseEntity<Void> removeFileFromPost(@PathVariable @NotNull Long postId,
                                                   @PathVariable @NotNull Long resourceId) {
        log.info("Removing file {} from post {}", resourceId, postId);
        postFileService.removeFileFromPost(postId, resourceId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Download file from post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File downloaded successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred during processing")
    })
    @GetMapping("/{postId}/resources/{resourceId}/download")
    public ResponseEntity<StreamingResponseBody> downloadFileFromPost(@PathVariable @NotNull Long postId,
                                                                      @PathVariable @NotNull Long resourceId) {
        log.info("Downloading file {} from post {}", resourceId, postId);
        return ResponseEntity.ok(postFileService.downloadFileFromPost(postId, resourceId));
    }

    @Operation(summary = "Download all files from post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files downloaded successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred during processing")
    })
    @GetMapping("/{postId}/resources/download")
    public ResponseEntity<StreamingResponseBody> downloadAllFileFromPost(@PathVariable @NotNull Long postId) {
        log.info("Downloading all files from post {}", postId);
        return ResponseEntity.ok(postFileService.downloadFilesFromPost(postId));
    }
}
