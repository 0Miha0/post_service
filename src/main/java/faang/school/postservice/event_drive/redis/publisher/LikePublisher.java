package faang.school.postservice.event_drive.redis.publisher;

import faang.school.postservice.event_drive.redis.event.LikeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikePublisher implements EventPublisher<LikeEvent>{

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.data.redis.channels.like}")
    private String channel;

    @Override
    public void publish(LikeEvent event) {
        redisTemplate.convertAndSend(channel, event);
    }
}
