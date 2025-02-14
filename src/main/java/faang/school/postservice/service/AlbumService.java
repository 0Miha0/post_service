package faang.school.postservice.service;

import faang.school.postservice.dto.album.AlbumDto;
import faang.school.postservice.dto.album.AlbumFilterDto;
import faang.school.postservice.filter.Filter;
import faang.school.postservice.mapper.AlbumMapper;
import faang.school.postservice.model.Album;
import faang.school.postservice.repository.AlbumRepository;
import faang.school.postservice.validator.AlbumValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumValidator albumValidator;
    private final AlbumRepository albumRepository;
    private final PostService postService;
    private final AlbumMapper albumMapper;
    private final List<Filter<Album, AlbumFilterDto>> filters;

    @Transactional
    public void createAlbum(AlbumDto dto, Long userId) {
        log.info("Creating new album for user {}", userId);
        albumValidator.doesExistUser(userId);
        Album album = Album.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .authorId(userId)
                .visibilityAlbums(dto.getVisibilityAlbums())
                .createdAt(LocalDateTime.now())
                .build();
        save(album);
        log.info("Created album with id: {}", album.getId());
    }

    @Transactional
    public void addPostToAlbum(Long postId, Long albumId, Long userId) {
        log.info("Adding post {} to album {}", postId, albumId);
        Album album = findById(albumId);
        albumValidator.doesAuthorUser(userId, album.getAuthorId());
        album.addPost(postService.findById(postId));
        save(album);
        log.info("Added post {} to album {}", postId, albumId);
    }

    @Transactional
    public void removePostFromAlbum(Long albumId, Long postId, Long userId) {
        log.info("Removing post {} from album {}", postId, albumId);
        Album album = findById(albumId);
        albumValidator.doesAuthorUser(userId, album.getAuthorId());
        album.removePost(postId);
        save(album);
        log.info("Removed post {} from album {}", postId, albumId);
    }

    public AlbumDto getAlbumById(Long albumId) {
        log.info("Getting album with id: {}", albumId);
        return albumMapper.toDto(findById(albumId));
    }

    public List<AlbumDto> getAllAuthorAlbumsWithFilter(AlbumFilterDto dto, Long authorId) {
        log.info("Getting all author albums with filter for user {}", authorId);
        return applyFilter(dto, findAllByAuthorId(authorId));
    }

    public List<AlbumDto> getAllAlbumsWithFilter(AlbumFilterDto dto) {
        log.info("Getting all albums with filter");
        return applyFilter(dto, findAll());
    }

    @Transactional
    public void updateAlbum(Long albumId, AlbumDto dto, Long userId) {
        log.info("Updating album {} with title: {}, description: {}", albumId, dto.getTitle(), dto.getDescription());
        Album album = findById(albumId);
        albumValidator.doesAuthorUser(userId, album.getAuthorId());
        album.setTitle(dto.getTitle());
        album.setDescription(dto.getDescription());
        save(album);
        log.info("Updated album {}", albumId);
    }

    @Transactional
    public void deleteAlbum(Long albumId) {
        log.info("Deleting album {}", albumId);
        deleteById(albumId);
        log.info("Deleted album {}", albumId);
    }

    public List<Album> findAllByAuthorId(Long authorId) {
        log.info("Finding all albums by author id: {}", authorId);
        return albumRepository.findAllByAuthorId(authorId);
    }

    public List<Album> findAll() {
        return albumRepository.findAll();
    }

    public Album findById(Long id) {
        log.info("Finding album with id: {}", id);
        return albumRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Album not found with id: {}", id);
                    return new EntityNotFoundException("Album not found with id: " + id);
                });
    }

    public void deleteById(Long id) {
        log.info("Deleting album with id: {}", id);
        albumRepository.deleteById(id);
        log.info("Deleted album with id: {}", id);
    }

    public void save(Album album) {
        log.info("Saving album with id: {}", album.getId());
        albumRepository.save(album);
        log.info("Saved album with id: {}", album.getId());
    }

    private List<AlbumDto> applyFilter(AlbumFilterDto dto, List<Album> albums) {
        List<Album> filteredAlbums = filters.stream()
                .filter(filter -> filter.isApplicable(dto))
                .reduce(albums.stream(),
                        (albumStream, filter) -> filter.apply(albumStream, dto),
                        (s1, s2) -> s2
                )
                .toList();
        log.info("Found {} albums after applying filter", filteredAlbums.size());
        return albumMapper.toDtoList(filteredAlbums);
    }
}
