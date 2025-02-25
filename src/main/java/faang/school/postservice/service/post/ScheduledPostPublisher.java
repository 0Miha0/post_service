package faang.school.postservice.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledPostPublisher {

    private final PostService postService;

    @Value("${app.post-service.scheduledPostPublisher.butch}")
    private int butchSize;

    @Scheduled(cron = "${scheduled.scheduledPostPublisher}")
    public void publishScheduledPosts() {
        log.info("Publishing scheduled posts in batches of {}", butchSize);
        postService.publishScheduledPosts(butchSize);
        log.info("Published scheduled posts in batches of {}", butchSize);
    }
}
