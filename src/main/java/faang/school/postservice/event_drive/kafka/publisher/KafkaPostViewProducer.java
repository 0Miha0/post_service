package faang.school.postservice.event_drive.kafka.publisher;

import faang.school.postservice.event_drive.kafka.event.KafkaPostViewDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaPostViewProducer implements EventPublisher<KafkaPostViewDto> {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final NewTopic topicPostViews;

    @Override
    public void publish(KafkaPostViewDto event) {
        kafkaTemplate.send(topicPostViews.name(), event);
    }
}