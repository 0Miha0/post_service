package faang.school.postservice.event_drive.kafka.consumer;

import faang.school.postservice.event_drive.kafka.event.KafkaPostDto;
import faang.school.postservice.service.FeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaPostConsumerTest {
    @Mock
    private FeedService feedService;
    @Mock
    private Acknowledgment acknowledgment;
    @InjectMocks
    private KafkaPostConsumer kafkaPostConsumer;

    private KafkaPostDto event;

    @BeforeEach
    void setUp() {
        event = mock(KafkaPostDto.class);
        when(event.getId()).thenReturn(1L);
    }

    @Test
    void testKafkaPostConsumerSuccess() {
        kafkaPostConsumer.listener(event, acknowledgment);

        verify(feedService, times(1))
                .addPostIdToAuthorSubscribers(eq(1L), any());
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    void testKafkaPostConsumerException() {
        doThrow(new RuntimeException("Error"))
                .when(feedService).addPostIdToAuthorSubscribers(anyLong(), any());

        Exception exception = assertThrows(RuntimeException.class,
                () -> kafkaPostConsumer.listener(event, acknowledgment));

        assertEquals("Error", exception.getMessage());
        verify(acknowledgment, never()).acknowledge();
    }
}
