package faang.school.postservice.event_drive.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaPostDto {
    private Long id;
    private Long authorId;
    private List<Long> subscriberIds;
}
