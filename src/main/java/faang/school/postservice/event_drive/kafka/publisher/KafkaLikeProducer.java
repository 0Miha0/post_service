package faang.school.postservice.event_drive.kafka.publisher;

import faang.school.postservice.event_drive.kafka.event.KafkaLikeDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaLikeProducer implements EventPublisher<KafkaLikeDto> {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final NewTopic topicLikes;

    @Override
    public void publish(KafkaLikeDto event) {
        kafkaTemplate.send(topicLikes.name(), event);
    }
}