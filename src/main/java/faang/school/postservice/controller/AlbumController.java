package faang.school.postservice.controller;

import faang.school.postservice.dto.album.AlbumDto;
import faang.school.postservice.dto.album.AlbumFilterDto;
import faang.school.postservice.service.AlbumService;
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

@Tag(name = "Album")
@RestController
@RequestMapping("/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @Operation(summary = "Create new album")
    @PostMapping
    public ResponseEntity<Void> createAlbum(@RequestBody AlbumDto dto,
                                            @RequestHeader("x-user-id") Long userId
                                            ) {
        albumService.createAlbum(dto, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Add post to album")
    @PostMapping("/{albumId}/posts/{postId}")
    public ResponseEntity<Void> addPostToAlbum(@PathVariable Long postId,
                                               @PathVariable Long albumId,
                                               @RequestHeader("x-user-id") Long userId
                                               ) {
        albumService.addPostToAlbum(postId, albumId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove post from album")
    @DeleteMapping("/{albumId}/posts/{postId}")
    public ResponseEntity<Void> removePostFromAlbum(@PathVariable Long albumId,
                                                    @PathVariable Long postId,
                                                    @RequestHeader("x-user-id") Long userId
                                                    ) {
        albumService.removePostFromAlbum(albumId, postId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get album by id")
    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumDto> getAlbumById(@PathVariable Long albumId) {
        return ResponseEntity.ok(albumService.getAlbumById(albumId));
    }

    @Operation(summary = "Get all author albums with filter")
    @PostMapping("/author/{authorId}/filter")
    public ResponseEntity<List<AlbumDto>> getAllAuthorAlbumsWithFilter(@RequestBody AlbumFilterDto dto,
                                                                       @RequestHeader("x-user-id") Long authorId) {
        return ResponseEntity.ok(albumService.getAllAuthorAlbumsWithFilter(dto, authorId));
    }

    @Operation(summary = "Get all albums with filter")
    @PostMapping("/filter")
    public ResponseEntity<List<AlbumDto>> getAllAlbumsWithFilter(@RequestBody AlbumFilterDto dto) {
        return ResponseEntity.ok(albumService.getAllAlbumsWithFilter(dto));
    }

    @Operation(summary = "Update album")
    @PutMapping("/{albumId}")
    public ResponseEntity<Void> updateAlbum(@PathVariable Long albumId,
                                            @RequestBody AlbumDto dto,
                                            @RequestHeader("x-user-id") Long userId) {
        albumService.updateAlbum(albumId, dto, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete album")
    @DeleteMapping("/{albumId}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long albumId) {
        albumService.deleteAlbum(albumId);
        return ResponseEntity.ok().build();
    }
}
