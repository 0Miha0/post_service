package faang.school.postservice.event_drive.kafka.event;

import java.util.List;

public record KafkaFeedHeaterDto(List<Long> userIds) {
}
