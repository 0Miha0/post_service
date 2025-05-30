package faang.school.postservice.event_drive.kafka.publisher;

import faang.school.postservice.event_drive.kafka.event.KafkaPostDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaPostProducer implements EventPublisher<KafkaPostDto> {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final NewTopic topicPosts;

    @Override
    public void publish(KafkaPostDto event) {
        kafkaTemplate.send(topicPosts.name(), event);
    }
}