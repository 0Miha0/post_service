package faang.school.postservice.event_drive.redis.publisher;

import faang.school.postservice.event_drive.redis.event.CommentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentPublisher implements EventPublisher<CommentEvent>{

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.data.redis.channels.comment}")
    private String channel;

    @Override
    public void publish(CommentEvent event) {
        redisTemplate.convertAndSend(channel, event);
    }
}
