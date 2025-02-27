package faang.school.postservice.event_drive.redis.publisher;

import faang.school.postservice.event_drive.redis.event.PostViewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostViewPublisher implements EventPublisher<PostViewEvent>{

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.data.redis.channels.post_view}")
    private String channel;

    @Override
    public void publish(PostViewEvent event) {
        redisTemplate.convertAndSend(channel, event);
    }
}
