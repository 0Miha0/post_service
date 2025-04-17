package faang.school.postservice.event_drive.kafka.consumer;

import faang.school.postservice.event_drive.kafka.event.KafkaLikeDto;
import faang.school.postservice.service.post.PostCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaLikeConsumerTest {
    @Mock
    private PostCacheService postCacheService;
    @Mock
    private Acknowledgment acknowledgment;
    @InjectMocks
    private KafkaLikeConsumer kafkaLikeConsumer;

    private KafkaLikeDto event;

    @BeforeEach
    void setUp() {
        event = new KafkaLikeDto(2L, 3L, 1L);
    }

    @Test
    void testKafkaLikeConsumerSuccess() {
        kafkaLikeConsumer.listener(event, acknowledgment);

        verify(postCacheService, times(1))
                .incrementPostLikes(eq(3L), eq(2L));
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    void testKafkaLikeConsumerException() {
        doThrow(new RuntimeException("Error"))
                .when(postCacheService).incrementPostLikes(anyLong(), anyLong());

        Exception exception = assertThrows(RuntimeException.class,
                () -> kafkaLikeConsumer.listener(event, acknowledgment));

        assertEquals("Error", exception.getMessage());
        verify(acknowledgment, never()).acknowledge();
    }
}