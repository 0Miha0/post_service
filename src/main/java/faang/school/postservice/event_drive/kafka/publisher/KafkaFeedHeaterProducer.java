package faang.school.postservice.event_drive.kafka.publisher;

import faang.school.postservice.event_drive.kafka.event.KafkaFeedHeaterDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaFeedHeaterProducer implements EventPublisher<KafkaFeedHeaterDto> {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final NewTopic topicFeedHeater;

    @Override
    public void publish(KafkaFeedHeaterDto event) {
        kafkaTemplate.send(topicFeedHeater.name(), event);
    }
}
