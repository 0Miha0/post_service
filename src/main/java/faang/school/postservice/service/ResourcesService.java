package faang.school.postservice.service;

import faang.school.postservice.model.Resource;
import faang.school.postservice.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourcesService {

    private final ResourceRepository resourceRepository;

    public List<Resource> findByIds(List<Long> ids) {
        log.info("Finding resources by ids: {}", ids);
        return resourceRepository.findAllById(ids);
    }
}
