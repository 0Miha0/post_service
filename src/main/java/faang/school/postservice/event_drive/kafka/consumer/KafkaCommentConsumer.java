package faang.school.postservice.event_drive.kafka.consumer;

import faang.school.postservice.event_drive.kafka.event.KafkaCommentDto;
import faang.school.postservice.service.post.PostCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaCommentConsumer {
    private final PostCacheService postCacheService;

    @KafkaListener(topics = "${spring.kafka.topic.comments:comments}")
    public void listener(KafkaCommentDto event, Acknowledgment acknowledgment) {
        try {
            postCacheService.addCommentToPostCache(event.getPostId(), event.getCommentDto());
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Comment with id:{} author id:{} is not added to post with id:{}.",
                    event.getCommentDto().getId(),
                    event.getCommentDto().getAuthorId(),
                    event.getPostId());
            throw e;
        }
    }
}