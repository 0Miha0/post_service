package faang.school.postservice.service.ad;

import faang.school.postservice.dto.ad.AdDto;
import faang.school.postservice.mapper.AdMapper;
import faang.school.postservice.model.ad.Ad;
import faang.school.postservice.repository.ad.AdRepository;
import faang.school.postservice.service.post.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final PostService postService;

    public AdDto createAd(AdDto dto) {
        log.info("Creating new ad");
        Ad ad = adMapper.toEntity(dto);
        ad.setPost(postService.findById(dto.getPost()));
        log.info("Ad created");
        return adMapper.toDto(save(ad));
    }

    public Ad save(Ad ad) {
        log.info("Saving ad");
        return adRepository.save(ad);
    }

    public Ad findById(Long id) {
        log.info("Fetching ad with id: {}", id);
        return adRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Ad not found with id: {}", id);
                    return new EntityNotFoundException("Ad not found with id: " + id);
                });
    }
}
