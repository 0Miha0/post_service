package faang.school.postservice.event_drive.redis.publisher;

import faang.school.postservice.event_drive.redis.event.PublishPostEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PublishPostPublisher implements EventPublisher<PublishPostEvent>{

    @Value("${spring.data.redis.channels.publish_post}")
    private String channel;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publish(PublishPostEvent event) {
        redisTemplate.convertAndSend(channel, event);
    }
}
