package faang.school.postservice.service.ad;

import faang.school.postservice.model.ad.Ad;
import faang.school.postservice.repository.ad.AdRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledExpiredAdRemover {

    private final AdRepository adRepository;

    @Scheduled(cron = "${scheduled.removeExpiredAdsCron}")
    public void removeExpiredAds() {
        log.info("Starting removal of expired ads with no appearances left");
        Optional<List<Ad>> optionalExpiredAds = adRepository.findExpiredAdsWithNoAppearancesLeft(LocalDateTime.now());
        List<Ad> expiredAds = optionalExpiredAds.orElse(Collections.emptyList());

        if (expiredAds.isEmpty()) {
            log.info("No expired ads found for removal");
            return;
        }
        adRepository.deleteAll(expiredAds);
        log.info("Successfully removed {} expired ads", expiredAds.size());
    }
}
