package faang.school.postservice.service;

import faang.school.postservice.dto.resource.ResourceDto;
import faang.school.postservice.model.Resource;
import faang.school.postservice.repository.ResourceRepository;
import faang.school.postservice.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourcesService {

    private final ResourceRepository resourceRepository;
    private final PostService postService;

    public void createResource(ResourceDto dto) {
        log.info("Creating new resource for post {}", dto.getPost());
        Resource resource = Resource.builder()
                .key(dto.getKey())
                .size(dto.getSize())
                .createdAt(LocalDateTime.now())
                .name(dto.getName())
                .type(dto.getType())
                .post(postService.findById(dto.getPost()))
                .build();
        log.info("Resource created for post {}", dto.getPost());
        save(resource);
        log.info("Resource created with ID: {}", resource.getId());
    }

    public List<Resource> findByIds(List<Long> ids) {
        log.info("Finding resources by ids: {}", ids);
        return resourceRepository.findAllById(ids);
    }

    public Resource findById(Long id) {
        log.info("Finding resource by id: {}", id);
        return resourceRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Resource not found with id: {}", id);
                    return new IllegalArgumentException("Resource not found with id: " + id);
                });
    }

    public void save(Resource resource) {
        log.info("Saving resource: {}", resource.getId());
        resourceRepository.save(resource);
        log.info("Saved resource with ID: {}", resource.getId());
    }

    public void deleteById(Long id) {
        log.info("Deleting resource with id: {}", id);
        resourceRepository.deleteById(id);
        log.info("Deleted resource with id: {}", id);
    }

    public List<Resource> findAllByPostId(Long postId) {
        log.info("Finding resources by post id: {}", postId);
        return resourceRepository.findAllByPostId(postId);
    }

    public Resource findByPostId(Long postId) {
        log.info("Finding resource by post id: {}", postId);
        return resourceRepository.findByPostId(postId);
    }
}
