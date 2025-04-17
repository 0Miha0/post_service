package faang.school.postservice.event_drive.kafka.consumer;

import faang.school.postservice.dto.comment.CommentDto;
import faang.school.postservice.event_drive.kafka.event.KafkaCommentDto;
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
class KafkaCommentConsumerTest {
    @Mock
    private PostCacheService postCacheService;
    @Mock
    private Acknowledgment acknowledgment;
    @InjectMocks
    private KafkaCommentConsumer kafkaCommentConsumer;

    private KafkaCommentDto event;

    @BeforeEach
    void setUp() {
        event = mock(KafkaCommentDto.class);
        when(event.getPostId()).thenReturn(1L);
        when(event.getCommentDto()).thenReturn(mock(CommentDto.class));
    }

    @Test
    void testKafkaCommentConsumerSuccess() {
        kafkaCommentConsumer.listener(event, acknowledgment);

        verify(postCacheService, times(1))
                .addCommentToPostCache(eq(1L), any(CommentDto.class));
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    void testKafkaCommentConsumerException() {
        doThrow(new RuntimeException("Error"))
                .when(postCacheService).addCommentToPostCache(anyLong(), any());

        Exception exception = assertThrows(RuntimeException.class,
                () -> kafkaCommentConsumer.listener(event, acknowledgment));

        assertEquals("Error", exception.getMessage());
        verify(acknowledgment, never()).acknowledge();
    }
}