package faang.school.postservice.event_drive.kafka.publisher;

import faang.school.postservice.event_drive.kafka.event.KafkaCommentDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaCommentProducer implements EventPublisher<KafkaCommentDto> {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final NewTopic topicComments;

    @Override
    public void publish(KafkaCommentDto event) {
        kafkaTemplate.send(topicComments.name(), event);
    }
}