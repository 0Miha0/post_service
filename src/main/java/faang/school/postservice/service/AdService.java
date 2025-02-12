package faang.school.postservice.service;

import faang.school.postservice.model.ad.Ad;
import faang.school.postservice.repository.ad.AdRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;

    public Ad findById(Long id) {
        log.info("Fetching ad with id: {}", id);
        return adRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Ad not found with id: {}", id);
                    return new EntityNotFoundException("Ad not found with id: " + id);
                });
    }
}
